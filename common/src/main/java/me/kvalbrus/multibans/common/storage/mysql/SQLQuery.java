package me.kvalbrus.multibans.common.storage.mysql;

public enum SQLQuery {

    CREATE_TABLE_PUNISHMENTS("CREATE TABLE IF NOT EXISTS punishments ("
        + "id                   VARCHAR(10)         NOT NULL PRIMARY KEY,"
        + "type                 VARCHAR(20)         NOT NULL,"
        + "target_ip            VARCHAR(15),"
        + "target_name          VARCHAR(29)         NOT NULL,"
        + "target_uuid          VARCHAR(49),"
        + "creator_name         VARCHAR(29)         NOT NULL,"
        + "date_created         BIGINT              NOT NULL,"
        + "date_start           BIGINT,"
        + "duration             BIGINT,"
        + "reason               VARCHAR(255)        NOT NULL,"
        + "comment              VARCHAR(999)        NOT NULL,"
        + "cancellation_creator VARCHAR(29),"
        + "cancellation_date    BIGINT,"
        + "cancellation_reason  VARCHAR(255),"
        + "servers              CHAR(255),"
        + "cancelled            BOOLEAN)"),

    CREATE_TABLE_ACTIONS("CREATE TABLE IF NOT EXISTS actions (" +
        "pun_id                 VARCHAR(10)         NOT NULL," +
        "id                     INT                 NOT NULL," +
        "type                   VARCHAR(20)         NOT NULL," +
        "executor               VARCHAR(29)         NOT NULL," +
        "date                   BIGINT              NOT NULL," +
        "reason                 VARCHAR(255)        NOT NULL)"),

    CREATE_PUNISHMENT("INSERT INTO punishments " +
        "(id, type, target_ip, target_name, target_uuid, creator_name, date_created, date_start, duration, reason, comment, cancellation_creator, cancellation_date, cancellation_reason, servers, cancelled) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

    UPDATE_PUNISHMENT(
        "UPDATE punishments SET date_start=?, duration=?, reason=?, comment=?, cancellation_creator=?, cancellation_date=?, cancellation_reason=?, servers=?, cancelled=? "
            + "WHERE id=?"),

    DELETE_PUNISHMENT("DELETE FROM punishments WHERE id=?"),

    DELETE_ACTIONS("DELETE FROM actions WHERE pun_id=?"),

    HAS_PUNISHMENT("SELECT * FROM punishments WHERE id=?"),

    HISTORY_PUNISHMENTS_TARGET_NAME("SELECT * FROM punishments WHERE target_name=?"),

    HISTORY_PUNISHMENTS_TARGET_UUID("SELECT * FROM punishments WHERE target_uuid=?"),

    HISTORY_PUNISHMENTS_CREATOR("SELECT * FROM punishments WHERE creator_name=?"),

    CREATE_ACTION("INSERT INTO actions (pun_id, id, type, executor, date, reason) VALUES (?, ?, ?, ?, ?, ?)"),

    HAS_ACTION("SELECT * FROM actions WHERE pun_id=? AND id=? AND type=?"),

    GET_PUNISHMENT_ACTIONS("SELECT * FROM actions WHERE pun_id=?"),

    WIPE_TABLE_PUNISHMENTS("TRUNCATE TABLE punishments"),

    WIPE_TABLE_ACTIONS("TRUNCATE TABLE actions"),

    DELETE_TABLE_PUNISHMENTS("DROP TABLE punishments"),

    DELETE_TABLE_ACTIONS("DROP TABLE actions");

    private final String query;

    SQLQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}