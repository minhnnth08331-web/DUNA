import { SetMetadata, createParamDecorator, ExecutionContext } from '@nestjs/common';

export const REQUIRE_PERMISSIONS_KEY = 'REQUIRE_PERMISSIONS';
export const RequirePermissions = (...permissions: string[]) =>
  SetMetadata(REQUIRE_PERMISSIONS_KEY, permissions);

export const IS_PUBLIC_KEY = 'IS_PUBLIC';
export const Public = () => SetMetadata(IS_PUBLIC_KEY, true);

export const IS_SELF_SERVICE_KEY = 'IS_SELF_SERVICE';
export const SelfService = () => SetMetadata(IS_SELF_SERVICE_KEY, true);

export const CurrentUser = createParamDecorator(
  (data: string | undefined, ctx: ExecutionContext) => {
    const request = ctx.switchToHttp().getRequest();
    const user = request.user;
    return data ? user?.[data] : user;
  },
);
