from datetime import datetime

from pydantic import BaseModel


class SavingsBase(BaseModel):
    savings_name: str
    amount: float
    date: datetime
    user_id: int


class SavingsUpdateSchema(BaseModel):
    savings_name: str | None = None
    amount: float | None = None


class SavingsCreateSchema(SavingsBase):
    pass


class SavingsSchema(SavingsBase):
    id: int
