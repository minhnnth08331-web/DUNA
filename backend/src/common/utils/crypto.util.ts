import * as argon2 from 'argon2';
import * as crypto from 'crypto';
import { authenticator } from 'otplib';

// Precomputed hash for dummy timing comparison
let DUMMY_HASH: string;
(async () => {
  try {
    DUMMY_HASH = await argon2.hash('DummyPassword123!', { type: argon2.argon2id });
  } catch (err) {
    // fallback dummy
    DUMMY_HASH = '$argon2id$v=19$m=65536,t=3,p=4$dHVtbXlzYWx0MTIzNDU2$0000000000000000000000000000000000000000000';
  }
})();

export async function hashPassword(password: string): Promise<string> {
  return argon2.hash(password, { type: argon2.argon2id });
}

export async function verifyPassword(hash: string, plain: string): Promise<boolean> {
  try {
    return await argon2.verify(hash, plain);
  } catch {
    return false;
  }
}

export async function dummyVerify(plain: string): Promise<boolean> {
  try {
    if (!DUMMY_HASH) {
      DUMMY_HASH = await argon2.hash('DummyPassword123!', { type: argon2.argon2id });
    }
    await argon2.verify(DUMMY_HASH, plain);
  } catch {}
  return false;
}

export function hashToken(token: string): string {
  return crypto.createHash('sha256').update(token).digest('hex');
}

// AES-256-GCM for totp_secret
function getEncryptionKey(): Buffer {
  const keyHex = process.env.TOTP_ENCRYPTION_KEY || '0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef';
  return Buffer.from(keyHex, 'hex');
}

export function encryptSecret(plainText: string): string {
  const iv = crypto.randomBytes(12);
  const cipher = crypto.createCipheriv('aes-256-gcm', getEncryptionKey(), iv);
  let encrypted = cipher.update(plainText, 'utf8', 'hex');
  encrypted += cipher.final('hex');
  const authTag = cipher.getAuthTag().toString('hex');
  return `${iv.toString('hex')}:${authTag}:${encrypted}`;
}

export function decryptSecret(cipherText: string): string {
  const parts = cipherText.split(':');
  if (parts.length !== 3) {
    throw new Error('Invalid encrypted format');
  }
  const [ivHex, authTagHex, encryptedHex] = parts;
  const decipher = crypto.createDecipheriv('aes-256-gcm', getEncryptionKey(), Buffer.from(ivHex, 'hex'));
  decipher.setAuthTag(Buffer.from(authTagHex, 'hex'));
  let decrypted = decipher.update(encryptedHex, 'hex', 'utf8');
  decrypted += decipher.final('utf8');
  return decrypted;
}

// TOTP Helpers (RFC 6238, 30s)
authenticator.options = {
  step: 30,
  window: 1, // allow +/- 1 step
};

export function generateTotpSecret(): string {
  return authenticator.generateSecret();
}

export function generateTotpUri(email: string, secret: string): string {
  return authenticator.keyuri(email, "DUNA Admin", secret);
}

export function verifyTotpCode(secret: string, code: string): boolean {
  try {
    return authenticator.verify({ token: code, secret });
  } catch {
    return false;
  }
}

export function getCurrentTotpStep(): bigint {
  return BigInt(Math.floor(Date.now() / 1000 / 30));
}
