from datetime import datetime

from pydantic import BaseModel


class SavingsBase(BaseModel):
    name: str
    amount: float
    date: datetime
    user_id: int


class SavingsUpdateSchema(BaseModel):
    name: str | None = None
    amount: float | None = None
    date: datetime | None = None
    user_id: int | None = None


class SavingsCreateSchema(SavingsBase):
    pass


class SavingsSchema(SavingsBase):
    id: int
