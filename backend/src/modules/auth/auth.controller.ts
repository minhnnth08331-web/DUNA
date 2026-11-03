import {
  Controller,
  Post,
  Get,
  Patch,
  Body,
  Req,
  Res,
  Param,
  UseGuards,
  HttpStatus,
  HttpCode,
  Headers,
} from '@nestjs/common';
import { Request, Response } from 'express';
import { JwtService } from '@nestjs/jwt';
import { AuthService } from './auth.service';
import { Public, SelfService, CurrentUser } from '../../common/decorators/rbac.decorator';
import { RbacGuard } from '../../common/guards/rbac.guard';
import {
  LoginDto,
  Confirm2faDto,
  ChangePasswordDto,
  ForgotPasswordDto,
  ResetPasswordDto,
  UpdateMeDto,
} from './dto/auth.dto';
import { BusinessException } from '../../common/filters/global-exception.filter';
import { ERROR_CODES } from '../../common/constants/error-codes';

@Controller('auth')
export class AuthController {
  constructor(
    private readonly authService: AuthService,
    private readonly jwtService: JwtService,
  ) {}

  @Post('login')
  @Public()
  @HttpCode(HttpStatus.OK)
  async login(
    @Body() dto: LoginDto,
    @Req() req: Request,
    @Res({ passthrough: true }) res: Response,
  ) {
    const ip =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';
    const userAgent = req.headers['user-agent'];

    const result = await this.authService.login(dto, ip, userAgent);

    if ('refreshToken' in result && result.refreshToken) {
      res.cookie('refreshToken', result.refreshToken, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        maxAge: 7 * 24 * 60 * 60 * 1000,
      });
    }

    return result;
  }

  @Post('2fa/setup')
  @Public()
  @HttpCode(HttpStatus.OK)
  async setup2fa(
    @Headers('authorization') authHeader: string,
    @CurrentUser('id') currentUserId: string,
  ) {
    let empId: bigint | null = null;
    let email = 'admin@duna.vn';

    if (authHeader && authHeader.startsWith('Bearer ')) {
      try {
        const token = authHeader.split(' ')[1];
        const payload: any = this.jwtService.verify(token, {
          secret: process.env.JWT_ACCESS_SECRET || 'duna_access_secret_key_32bytes_min_dev_2026',
        });
        empId = BigInt(payload.sub);
      } catch {}
    }

    if (!empId && currentUserId) {
      empId = BigInt(currentUserId);
    }

    if (!empId) {
      throw new BusinessException(
        ERROR_CODES.AUTH_TOKEN_EXPIRED,
        'Yêu cầu setupToken hoặc access token hợp lệ.',
        HttpStatus.UNAUTHORIZED,
      );
    }

    return this.authService.setup2fa(empId, email);
  }

  @Post('2fa/confirm')
  @Public()
  @HttpCode(HttpStatus.OK)
  async confirm2fa(
    @Body() dto: Confirm2faDto,
    @Headers('authorization') authHeader: string,
    @CurrentUser('id') currentUserId: string,
  ) {
    let empId: bigint | null = null;

    if (authHeader && authHeader.startsWith('Bearer ')) {
      try {
        const token = authHeader.split(' ')[1];
        const payload: any = this.jwtService.verify(token, {
          secret: process.env.JWT_ACCESS_SECRET || 'duna_access_secret_key_32bytes_min_dev_2026',
        });
        empId = BigInt(payload.sub);
      } catch {}
    }

    if (!empId && currentUserId) {
      empId = BigInt(currentUserId);
    }

    if (!empId) {
      throw new BusinessException(
        ERROR_CODES.AUTH_TOKEN_EXPIRED,
        'Yêu cầu setupToken hoặc access token hợp lệ.',
        HttpStatus.UNAUTHORIZED,
      );
    }

    return this.authService.confirm2fa(empId, dto.code, dto.secret || '');
  }

  @Post('refresh')
  @Public()
  @HttpCode(HttpStatus.OK)
  async refresh(
    @Req() req: Request,
    @Res({ passthrough: true }) res: Response,
    @Body('refreshToken') bodyRefreshToken?: string,
  ) {
    const token = req.cookies?.refreshToken || bodyRefreshToken;
    const ip =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';
    const userAgent = req.headers['user-agent'];

    const result = await this.authService.refreshTokens(token, ip, userAgent);

    res.cookie('refreshToken', result.refreshToken, {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'strict',
      maxAge: 7 * 24 * 60 * 60 * 1000,
    });

    return { accessToken: result.accessToken };
  }

  @Post('logout')
  @SelfService()
  @UseGuards(RbacGuard)
  @HttpCode(HttpStatus.OK)
  async logout(
    @CurrentUser('sid') sid: string,
    @Res({ passthrough: true }) res: Response,
  ) {
    await this.authService.logout(sid);
    res.clearCookie('refreshToken');
    return { message: 'Đăng xuất thành công.' };
  }

  @Post('logout-all')
  @SelfService()
  @UseGuards(RbacGuard)
  @HttpCode(HttpStatus.OK)
  async logoutAll(
    @CurrentUser('id') id: string,
    @Res({ passthrough: true }) res: Response,
  ) {
    await this.authService.logoutAll(BigInt(id));
    res.clearCookie('refreshToken');
    return { message: 'Đã đăng xuất toàn bộ các phiên.' };
  }

  @Get('me')
  @SelfService()
  @UseGuards(RbacGuard)
  async getMe(@CurrentUser('id') id: string) {
    return this.authService.getMe(BigInt(id));
  }

  @Patch('me')
  @SelfService()
  @UseGuards(RbacGuard)
  async updateMe(@CurrentUser('id') id: string, @Body() dto: UpdateMeDto) {
    return this.authService.updateMe(BigInt(id), dto);
  }

  @Get('sessions')
  @SelfService()
  @UseGuards(RbacGuard)
  async getSessions(@CurrentUser('id') id: string) {
    return this.authService.getSessions(BigInt(id));
  }

  @Post('sessions/:sessionId/revoke')
  @SelfService()
  @UseGuards(RbacGuard)
  @HttpCode(HttpStatus.OK)
  async revokeSession(
    @Param('sessionId') sessionId: string,
    @CurrentUser() user: any,
  ) {
    return this.authService.revokeSession(sessionId, user);
  }

  @Post('change-password')
  @SelfService()
  @UseGuards(RbacGuard)
  @HttpCode(HttpStatus.OK)
  async changePassword(
    @CurrentUser('id') id: string,
    @CurrentUser('sid') sid: string,
    @Body() dto: ChangePasswordDto,
  ) {
    return this.authService.changePassword(BigInt(id), dto, sid);
  }

  @Post('forgot-password')
  @Public()
  @HttpCode(HttpStatus.OK)
  async forgotPassword(@Body() dto: ForgotPasswordDto, @Req() req: Request) {
    const ip =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';
    return this.authService.forgotPassword(dto, ip);
  }

  @Post('reset-password')
  @Public()
  @HttpCode(HttpStatus.OK)
  async resetPassword(@Body() dto: ResetPasswordDto, @Req() req: Request) {
    const ip =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';
    return this.authService.resetPassword(dto, ip);
  }
}
