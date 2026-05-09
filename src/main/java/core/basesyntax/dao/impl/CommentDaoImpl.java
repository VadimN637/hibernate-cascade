package core.basesyntax.dao.impl;

import java.util.List;
import core.basesyntax.dao.CommentDao;
import core.basesyntax.exception.DataProcessingException;
import core.basesyntax.model.Comment;
import core.basesyntax.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CommentDaoImpl extends AbstractDao implements CommentDao {
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Comment create(Comment entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create Comment in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Comment get(Long id) {
        try (Session session = factory.openSession()) {
            return session.get(Comment.class, id);
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get Comment by id: " + id, e);
        }
    }

    @Override
    public List<Comment> getAll() {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Comment", Comment.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(
                    "Can't get the list of Comments", e);
        }
    }

    @Override
    public void remove(Comment entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, entity.getId());
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't remove Comment from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
