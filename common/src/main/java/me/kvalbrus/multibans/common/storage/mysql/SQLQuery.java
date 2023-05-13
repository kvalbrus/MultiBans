package me.kvalbrus.multibans.common.storage.mysql;

public enum SQLQuery {
    CREATE_TABLE_PUNISHMENTS("CREATE TABLE IF NOT EXISTS punishments ("
        + "id VARCHAR(10) NOT NULL PRIMARY KEY,"
        + "type VARCHAR(20) NOT NULL,"
        + "target_ip VARCHAR(15),"
        + "target_name VARCHAR(29) NOT NULL,"
        + "target_uuid VARCHAR(49),"
        + "creator_name VARCHAR(29) NOT NULL,"
        + "date_created BIGINT NOT NULL,"
        + "date_start BIGINT,"
        + "duration BIGINT,"
        + "reason VARCHAR(255),"
        + "comment VARCHAR(999),"
        + "cancellation_creator VARCHAR(29),"
        + "cancellation_date BIGINT,"
        + "cancellation_reason VARCHAR(255),"
        + "servers CHAR(255),"
        + "cancelled BOOLEAN)"),

    DELETE_TABLE_PUNISHMENT("DROP TABLE punishments"),

    CREATE_PUNISHMENT("INSERT INTO punishments " +
        "(id, type, target_ip, target_name, target_uuid, creator_name, date_created, date_start, duration, reason, comment, cancellation_creator, cancellation_date, cancellation_reason, servers, cancelled) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

    UPDATE_PUNISHMENT(
        "UPDATE punishments SET date_start=?, duration=?, reason=?, comment=?, cancellation_creator=?, cancellation_date=?, cancellation_reason=?, servers=?, cancelled=? "
            + "WHERE id=?"),

    DELETE_PUNISHMENT("DELETE FROM punishments WHERE id=?"),

    HAS_PUNISHMENT("SELECT * FROM punishments WHERE id=?"),

    HISTORY_PUNISHMENTS_TARGET_NAME("SELECT * FROM punishments WHERE target_name=?"),

    HISTORY_PUNISHMENTS_TARGET_UUID("SELECT * FROM punishments WHERE target_uuid=?"),

    HISTORY_PUNISHMENTS_CREATOR("SELECT * FROM punishments WHERE creator_name=?"),

    WIPE_TABLE_PUNISHMENTS("TRUNCATE TABLE punishments");

    private final String query;

    SQLQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}