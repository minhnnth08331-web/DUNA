import { IsEmail, IsNotEmpty, IsOptional, IsString, Matches, MinLength } from 'class-validator';

export class CreateEmployeeDto {
  @IsEmail({}, { message: 'Email không hợp lệ.' })
  @IsNotEmpty({ message: 'Email không được để trống.' })
  email: string;

  @IsString()
  @IsNotEmpty({ message: 'Họ và tên không được để trống.' })
  fullName: string;

  @IsString()
  @Matches(/^(0)(3|5|7|8|9)[0-9]{8}$/, { message: 'Số điện thoại không hợp lệ (chuẩn VN 10 số).' })
  phone: string;

  @IsString()
  @IsNotEmpty({ message: 'Vai trò không được để trống.' })
  roleCode: string;

  @IsOptional()
  @IsString()
  @MinLength(8, { message: 'Mật khẩu khởi tạo phải có ít nhất 8 ký tự.' })
  initialPassword?: string;
}

export class UpdateEmployeeDto {
  @IsOptional()
  @IsString()
  fullName?: string;

  @IsOptional()
  @IsString()
  @Matches(/^(0)(3|5|7|8|9)[0-9]{8}$/, { message: 'Số điện thoại không hợp lệ (chuẩn VN 10 số).' })
  phone?: string;

  @IsOptional()
  @IsEmail({}, { message: 'Email không hợp lệ.' })
  email?: string;
}

export class UpdateStatusRoleDto {
  @IsOptional()
  @IsString()
  status?: string; // ACTIVE, LOCKED, DISABLED

  @IsOptional()
  @IsString()
  roleCode?: string;

  @IsOptional()
  @IsString()
  reason?: string;
}
