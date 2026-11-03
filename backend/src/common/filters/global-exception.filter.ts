import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Request, Response } from 'express';
import { ERROR_CODES, ERROR_MESSAGES } from '../constants/error-codes';

export class BusinessException extends HttpException {
  constructor(
    public readonly code: string,
    message?: string,
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    public readonly details: Array<{ field?: string; message: string }> = [],
  ) {
    super(
      {
        code,
        message: message || ERROR_MESSAGES[code] || 'Lỗi hệ thống.',
        details,
      },
      status,
    );
  }
}

@Catch()
export class GlobalExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(GlobalExceptionFilter.name);

  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();

    const requestId =
      (request.headers['x-request-id'] as string) ||
      (request as any).requestId ||
      'req-' + Date.now();

    let status = HttpStatus.INTERNAL_SERVER_ERROR;
    let code = 'INTERNAL_SERVER_ERROR';
    let message = 'Đã có lỗi xảy ra từ phía máy chủ.';
    let details: Array<{ field?: string; message: string }> = [];

    if (exception instanceof BusinessException) {
      status = exception.getStatus();
      code = exception.code;
      message = exception.message;
      details = exception.details;
    } else if (exception instanceof HttpException) {
      status = exception.getStatus();
      const res = exception.getResponse();

      if (typeof res === 'string') {
        message = res;
      } else if (typeof res === 'object' && res !== null) {
        const resObj = res as Record<string, any>;
        code = resObj.code || (status === 403 ? ERROR_CODES.PERMISSION_DENIED : ERROR_CODES.VALIDATION_FAILED);
        message = resObj.message || ERROR_MESSAGES[code] || exception.message;
        if (Array.isArray(resObj.message)) {
          details = resObj.message.map((msg: string) => ({ message: msg }));
          message = ERROR_MESSAGES.VALIDATION_FAILED;
        } else if (resObj.details) {
          details = resObj.details;
        }
      }
    } else {
      this.logger.error(`Unhandled Exception on ${request.method} ${request.url}:`, exception);
    }

    if (status === HttpStatus.TOO_MANY_REQUESTS) {
      response.setHeader('Retry-After', '60');
    }

    response.setHeader('X-Request-Id', requestId);
    response.status(status).json({
      error: {
        code,
        message,
        details,
        requestId,
      },
    });
  }
}
