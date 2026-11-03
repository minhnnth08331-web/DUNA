import {
  ValidationPipe,
  ValidationError,
  HttpStatus,
  Injectable,
  ArgumentMetadata,
} from '@nestjs/common';
import { BusinessException } from '../filters/global-exception.filter';
import { ERROR_CODES, ERROR_MESSAGES } from '../constants/error-codes';

@Injectable()
export class CustomValidationPipe extends ValidationPipe {
  constructor() {
    super({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      exceptionFactory: (errors: ValidationError[]) => {
        const details: Array<{ field: string; message: string }> = [];

        function extractErrors(errs: ValidationError[], prefix = '') {
          for (const err of errs) {
            const fieldPath = prefix ? `${prefix}.${err.property}` : err.property;
            if (err.constraints) {
              for (const msg of Object.values(err.constraints)) {
                details.push({ field: fieldPath, message: msg });
              }
            }
            if (err.children && err.children.length > 0) {
              extractErrors(err.children, fieldPath);
            }
          }
        }

        extractErrors(errors);

        return new BusinessException(
          ERROR_CODES.VALIDATION_FAILED,
          ERROR_MESSAGES.VALIDATION_FAILED,
          HttpStatus.BAD_REQUEST,
          details,
        );
      },
    });
  }

  async transform(value: any, metadata: ArgumentMetadata) {
    return super.transform(value, metadata);
  }
}
