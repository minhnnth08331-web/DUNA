import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { TestEndpointsController } from './test-endpoints.controller';

@Module({
  imports: [JwtModule.register({})],
  controllers: [TestEndpointsController],
})
export class TestSupportModule {}
