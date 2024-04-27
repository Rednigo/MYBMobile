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
    password: SecretStr | None = None


class UserSchema(UserUpdateSchema):
    pass
