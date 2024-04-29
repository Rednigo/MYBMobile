from core.models.models import Base, engine


def create_tables():
    Base.metadata.create_all(bind=engine)


def drop_tables():
    Base.metadata.drop_all(bind=engine)


if __name__ == "__main__":
    create_tables()
    # drop_tables()
