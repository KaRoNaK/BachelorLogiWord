package com.bachelor.logiword.server.dao.account;

import com.bachelor.logiword.server.model.account.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository("accountEm")
public class AccountDataAccess implements AccountDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void register(Account acc) {
        em.createNativeQuery("insert into D_PLAYER (ROW_ID, PLAYER_ID, PLAYER_NAME, PASSWORD, MAIL, VALID_FROM, VALID_TO) " +
                "values (D_PLAYER_ROW_ID.nextval, D_PLAYER_PLAYER_ID.nextval, ?, GET_HASH(?, ?), " +
                "?, SYSDATE, null)")
                .setParameter(1, acc.getUsername())
                .setParameter(2, acc.getUsername())
                .setParameter(3, acc.getPassword())
                .setParameter(4, acc.getMail())
                .executeUpdate();
    }

    @Override
    public int login(String username, String password) {
        return ((BigDecimal) em.createNativeQuery("select PLAYER_ID from D_PLAYER " +
                "where PLAYER_NAME = ? and PASSWORD = GET_HASH(?, ?) and VALID_TO is null ")
                .setParameter(1, username)
                .setParameter(2, username)
                .setParameter(3, password)
                .getResultList().get(0)).intValue();
    }

    @Override
    @Transactional
    public void update(Account acc) {
        em.createNativeQuery("update D_PLAYER set VALID_TO = SYSDATE " +
                "where PLAYER_ID = ? and VALID_TO is null ")
                .setParameter(1, acc.getPlayerId())
                .executeUpdate();

        em.createNativeQuery("insert into D_PLAYER (ROW_ID, PLAYER_ID, PLAYER_NAME, PASSWORD, MAIL, VALID_FROM, VALID_TO) " +
                "values (D_PLAYER_ROW_ID.nextval, ? , ?, GET_HASH(?, ?), " +
                "?, SYSDATE, null)")
                .setParameter(1, acc.getPlayerId())
                .setParameter(2, acc.getUsername())
                .setParameter(3, acc.getUsername())
                .setParameter(4, acc.getPassword())
                .setParameter(5, acc.getMail())
                .executeUpdate();

    }
}
