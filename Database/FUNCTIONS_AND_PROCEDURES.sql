create or replace function get_hash (p_name  IN  VARCHAR2, p_password  IN  VARCHAR2)
    RETURN VARCHAR2 AS
    l_salt VARCHAR2(30) := 'dm9u84FHaPWfv0pTFqSJuiK356aFAC';
  BEGIN
    RETURN DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW(p_password || p_name || l_salt),DBMS_CRYPTO.HASH_SH1);
  END get_hash;

create or replace function get_daily_challenge_for_today
    return varchar2 is
    yesterday_word        varchar2(64);
    tomorrow_word     varchar2(64);
    has_today_word number;
    row_n             number := 0;
    row_count         number;
    now               date   := sysdate;
begin
    select count(*)
    into has_today_word
    from D_DAILY_CHALLENGE
    where VALID_FROM >= TRUNC(now)
      and VALID_TO <= TRUNC(now + 1) - 1 / 86400;
    if has_today_word = 0 then
        select WORD
        into yesterday_word
        from D_DAILY_CHALLENGE
        where VALID_FROM >= TRUNC(now - 1)
          and VALID_TO <= TRUNC(now) - 1 / 86400;

        select count(WORD_CREATED)
        into row_count
        from (
                 select WORD_CREATED, count(WORD_CREATED) as count
                 from F_SINGLE_PLAYER_GAME
                          inner join D_SINGLE_PLAYER_GAME on F_SINGLE_PLAYER_GAME.GAME_ID = D_SINGLE_PLAYER_GAME.ID
                 where END_TIME > now - 7
                 group by WORD_CREATED
                 order by count);
        while row_n < row_count
            loop
                row_n := row_n + 1;
                select WORD_CREATED
                into tomorrow_word
                from (
                         select WORD_CREATED, count(WORD_CREATED) as count
                         from F_SINGLE_PLAYER_GAME
                                  inner join D_SINGLE_PLAYER_GAME
                                             on F_SINGLE_PLAYER_GAME.GAME_ID = D_SINGLE_PLAYER_GAME.ID
                         where END_TIME > sysdate - 7
                         group by WORD_CREATED
                         order by count)
                where rownum = row_n;
                if (tomorrow_word != yesterday_word) then
                    return tomorrow_word;
                end if;
            end loop;
    end if;
    return null;
end get_daily_challenge_for_today;

create or replace procedure insert_daily_challenge_for_today(word varchar2)
as
begin
    insert into D_DAILY_CHALLENGE(ID, WORD, VALID_FROM, VALID_TO)
    VALUES (D_DAILY_CHALLENGE_ID.nextval, word, TRUNC(sysdate), TRUNC(sysdate + 1) - 1 / 86400);
end insert_daily_challenge_for_today;

select get_daily_challenge_for_today()
from dual;
Select *
from user_errors;

--Usage example
call INSERT_DAILY_CHALLENGE_FOR_TODAY(get_daily_challenge_for_today());
call INSERT_DAILY_CHALLENGE_FOR_TODAY('TEST');