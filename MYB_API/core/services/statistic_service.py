from datetime import datetime

from sqlalchemy import func, extract
from sqlalchemy.orm import Session

from core.models.models import Income, Expense, Savings


def get_financial_summary(user_id: int, db: Session):
    # Start by finding the earliest date across all transactions to define the reporting start
    earliest_income = db.query(func.min(Income.date)).scalar()
    earliest_expense = db.query(func.min(Expense.date)).scalar()
    earliest_savings = db.query(func.min(Savings.date)).scalar()
    start_date = min([d for d in [earliest_income, earliest_expense, earliest_savings] if d is not None])

    financial_data = {
        "months": [],
        "incomes": [],
        "expenses": [],
        "saved": []
    }

    if start_date is None:
        return financial_data  # No data available

    # Iterate through each month from start_date to the current month
    current_date = datetime.utcnow()
    while start_date <= current_date:
        year, month = start_date.year, start_date.month

        # Calculate totals for the month
        monthly_income = db.query(func.sum(Income.amount)).filter(
            extract('year', Income.date) == year,
            extract('month', Income.date) == month
        ).scalar() or 0

        monthly_expenses = db.query(func.sum(Expense.amount)).filter(
            extract('year', Expense.date) == year,
            extract('month', Expense.date) == month
        ).scalar() or 0

        monthly_savings = db.query(func.sum(Savings.amount)).filter(
            extract('year', Savings.date) == year,
            extract('month', Savings.date) == month
        ).scalar() or 0

        # Append results for the month
        financial_data['months'].append(f"{year}-{month:02d}")
        financial_data['incomes'].append(float(monthly_income))
        financial_data['expenses'].append(float(monthly_expenses))
        financial_data['saved'].append(float(monthly_income - monthly_expenses - monthly_savings))

        # Move to the next month
        if month == 12:
            start_date = datetime(year+1, 1, 1)
        else:
            start_date = datetime(year, month+1, 1)

    return financial_data
