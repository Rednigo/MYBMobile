from datetime import datetime
from pydantic import BaseModel


class ExpenseBase(BaseModel):
    name: str
    amount: float
    date: datetime
    user_id: int


class ExpenseUpdateSchema(BaseModel):
    name: str | None = None
    amount: float | None = None
    date: datetime | None = None
    category_id: int | None = None


class ExpenseCreateSchema(ExpenseBase):
    pass


class ExpenseSchema(ExpenseBase):
    id: int
