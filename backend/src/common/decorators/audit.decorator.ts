import { SetMetadata } from '@nestjs/common';

export interface AuditOptions {
  action: string;
  entityType: string;
  getEntityId?: (req: any, resData: any) => string | null;
}

export const AUDIT_KEY = 'AUDIT_OPTIONS';
export const Audit = (options: AuditOptions) => SetMetadata(AUDIT_KEY, options);
