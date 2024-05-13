from datetime import datetime

from pydantic import BaseModel


class IncomeBase(BaseModel):
    income_name: str
    amount: float
    date: datetime
    user_id: int


class IncomeUpdateSchema(BaseModel):
    income_name: str | None = None
    amount: float | None = None


class IncomeCreateSchema(IncomeBase):
    pass


class IncomeSchema(IncomeBase):
    id: int
