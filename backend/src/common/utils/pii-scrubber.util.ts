export function maskPhone(phone?: string | null): string | null {
  if (!phone) return null;
  const cleaned = phone.trim();
  if (cleaned.length < 7) return '***';
  return `${cleaned.slice(0, 3)}****${cleaned.slice(-3)}`;
}

export function maskEmail(email?: string | null): string | null {
  if (!email) return null;
  const parts = email.trim().split('@');
  if (parts.length !== 2) return '***@***';
  const name = parts[0];
  const domain = parts[1];
  const maskedName = name.length > 1 ? `${name[0]}***` : '*';
  return `${maskedName}@${domain}`;
}

const SENSITIVE_KEYS = new Set([
  'password',
  'passwordhash',
  'password_hash',
  'initialpassword',
  'initial_password',
  'totpsecret',
  'totp_secret',
  'token',
  'tokenhash',
  'token_hash',
  'refreshtoken',
  'refresh_token',
  'refreshtokenhash',
  'refresh_token_hash',
  'codehash',
  'code_hash',
  'secret',
]);

export function scrubPIIAndSecrets(obj: any): any {
  if (obj === null || obj === undefined) return obj;
  if (typeof obj === 'string') return obj;
  if (typeof obj === 'number' || typeof obj === 'boolean' || typeof obj === 'bigint') return obj;

  if (Array.isArray(obj)) {
    return obj.map(item => scrubPIIAndSecrets(item));
  }

  if (typeof obj === 'object') {
    const scrubbed: Record<string, any> = {};
    for (const [key, value] of Object.entries(obj)) {
      const lowerKey = key.toLowerCase();
      if (SENSITIVE_KEYS.has(lowerKey)) {
        continue; // Strip sensitive key entirely
      }

      if (lowerKey.includes('phone') && typeof value === 'string') {
        scrubbed[key] = maskPhone(value);
      } else if (lowerKey.includes('email') && typeof value === 'string') {
        scrubbed[key] = maskEmail(value);
      } else if ((lowerKey.includes('street') || lowerKey.includes('address')) && typeof value === 'string') {
        scrubbed[key] = '*** [Đã che địa chỉ chi tiết]';
      } else {
        scrubbed[key] = scrubPIIAndSecrets(value);
      }
    }
    return scrubbed;
  }

  return obj;
}
