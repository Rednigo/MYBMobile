from pydantic import BaseModel, EmailStr, SecretStr


class UserBaseSchema(BaseModel):
    email: EmailStr


class UserCreateSchema(UserBaseSchema):
    username: str
    password: str


class UserLoginSchema(BaseModel):
    username: str
    password: str


class UserUpdateSchema(UserCreateSchema):
    email: EmailStr | None = None
    username: str | None = None
    language: str | None = None
    is_light_scheme: bool | None = None
    password: SecretStr | None = None


class UserSchema(BaseModel):
    id: int
    email: EmailStr
    username: str
    language: str
    is_light_scheme: bool | None = None


class UpdateSettings(BaseModel):
    id: int
    username: str
    language: str
    is_light_scheme: bool | None = None
