import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { scrubPIIAndSecrets } from '../../common/utils/pii-scrubber.util';

export interface CreateAuditLogParams {
  actorEmpId?: bigint | null;
  action: string;
  entityType: string;
  entityId?: string | null;
  beforeState?: any;
  afterState?: any;
  ipAddress?: string | null;
  userAgent?: string | null;
  requestId?: string | null;
}

@Injectable()
export class AuditService {
  constructor(private readonly prisma: PrismaService) {}

  async log(params: CreateAuditLogParams) {
    // INV-22: Always scrub PII and secrets before writing to audit_logs
    const scrubbedBefore = params.beforeState ? scrubPIIAndSecrets(params.beforeState) : null;
    const scrubbedAfter = params.afterState ? scrubPIIAndSecrets(params.afterState) : null;

    return this.prisma.auditLog.create({
      data: {
        actorEmpId: params.actorEmpId ?? null,
        action: params.action,
        entityType: params.entityType,
        entityId: params.entityId ?? null,
        beforeState: scrubbedBefore,
        afterState: scrubbedAfter,
        ipAddress: params.ipAddress ?? null,
        userAgent: params.userAgent ?? null,
        requestId: params.requestId ?? null,
      },
    });
  }

  async findLogs(query: {
    actorEmpId?: string;
    action?: string;
    entityType?: string;
    entityId?: string;
    requestId?: string;
    limit?: number;
    cursor?: string;
  }) {
    const limit = Math.min(query.limit ? Number(query.limit) : 20, 100);
    const where: any = {};

    if (query.actorEmpId) where.actorEmpId = BigInt(query.actorEmpId);
    if (query.action) where.action = query.action;
    if (query.entityType) where.entityType = query.entityType;
    if (query.entityId) where.entityId = query.entityId;
    if (query.requestId) where.requestId = query.requestId;

    const items = await this.prisma.auditLog.findMany({
      where,
      take: limit + 1,
      cursor: query.cursor ? { id: BigInt(query.cursor) } : undefined,
      orderBy: { id: 'desc' },
      include: {
        employee: {
          select: {
            id: true,
            employeeCode: true,
            fullName: true,
            email: true,
          },
        },
      },
    });

    let nextCursor: string | null = null;
    if (items.length > limit) {
      const nextItem = items.pop();
      nextCursor = nextItem!.id.toString();
    }

    return {
      data: items,
      meta: {
        limit,
        nextCursor,
      },
    };
  }

  async findById(id: bigint) {
    return this.prisma.auditLog.findUnique({
      where: { id },
      include: {
        employee: {
          select: {
            id: true,
            employeeCode: true,
            fullName: true,
            email: true,
          },
        },
      },
    });
  }
}
