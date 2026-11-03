import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import * as cookieParser from 'cookie-parser';
import * as crypto from 'crypto';
import { GlobalExceptionFilter } from './common/filters/global-exception.filter';
import { TransformInterceptor } from './common/interceptors/transform.interceptor';
import { CustomValidationPipe } from './common/pipes/custom-validation.pipe';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.setGlobalPrefix('api/v1/admin');

  app.use(cookieParser());

  // Request ID middleware
  app.use((req: any, res: any, next: () => void) => {
    const requestId = req.headers['x-request-id'] || crypto.randomUUID();
    req.requestId = requestId;
    res.setHeader('X-Request-Id', requestId);
    next();
  });

  // CORS
  const origin = process.env.ADMIN_PORTAL_ORIGIN || 'http://localhost:3000';
  app.enableCors({
    origin: [origin, 'http://localhost:3000', 'https://admin.duna.vn'],
    credentials: true,
  });

  app.useGlobalPipes(new CustomValidationPipe());
  app.useGlobalFilters(new GlobalExceptionFilter());
  app.useGlobalInterceptors(new TransformInterceptor());

  const port = process.env.PORT || 3001;
  await app.listen(port);
  console.log(`DUNA Admin Portal Backend running on http://localhost:${port}/api/v1/admin`);
}

if (require.main === module) {
  bootstrap();
}
