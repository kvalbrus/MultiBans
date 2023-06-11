package me.kvalbrus.multibans.common.storage.mysql

object SQLQuery {

    private const val TABLE_PREFIX = "multibans_"
    private const val TABLE_PUNISHMENTS = "${TABLE_PREFIX}punishments"
    private const val TABLE_ACTIONS = "${TABLE_PREFIX}actions"
    private const val TABLE_PLAYER_SESSIONS = "${TABLE_PREFIX}player_sessions"

    const val CREATE_TABLE_PUNISHMENTS = """
        CREATE TABLE IF NOT EXISTS $TABLE_PUNISHMENTS (
            id                  VARCHAR(10)         NOT NULL PRIMARY KEY,
            type                VARCHAR(20)         NOT NULL,
            target_uuid         VARCHAR(36),
            target_ip           VARCHAR(15),
            target_name         VARCHAR(255)        NOT NULL,
            creator_name        VARCHAR(255)        NOT NULL,
            create_date         BIGINT              NOT NULL,
            start_date          BIGINT,
            duration            BIGINT,
            reason              VARCHAR(255)        NOT NULL,
            comment             VARCHAR(999)        NOT NULL,
            servers             VARCHAR(255),
            cancelled           BOOLEAN             NOT NULL
        )"""

    const val CREATE_TABLE_ACTIONS = """
        CREATE TABLE IF NOT EXISTS $TABLE_ACTIONS (
            pun_id              VARCHAR(10)         NOT NULL,
            id                  INT                 NOT NULL,
            type                VARCHAR(20)         NOT NULL,
            executor            VARCHAR(29)         NOT NULL,
            date                BIGINT              NOT NULL,
            reason              VARCHAR(255)        NOT NULL
        )"""

    const val CREATE_TABLE_PLAYER_SECTIONS = """
        CREATE TABLE IF NOT EXISTS $TABLE_PLAYER_SESSIONS (
            uuid                VARCHAR(36)         NOT NULL,
            name                VARCHAR(255)        NOT NULL,
            ip                  VARCHAR(15)         NOT NULL,
            join_time           BIGINT              NOT NULL,
            quit_time           BIGINT              NOT NULL
        )"""

    const val CREATE_PUNISHMENT = """
        INSERT INTO $TABLE_PUNISHMENTS (id, type, target_uuid, target_ip, target_name, creator_name,
        create_date, start_date, duration, reason, comment, servers, cancelled) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """

    const val UPDATE_PUNISHMENT = """
        UPDATE $TABLE_PUNISHMENTS SET
        start_date=?,
        duration=?,
        reason=?,
        comment=?,
        servers=?,
        cancelled=? WHERE id=?
    """

    const val DELETE_PUNISHMENT = "DELETE FROM $TABLE_PUNISHMENTS WHERE id=?"
    const val GET_PUNISHMENT = "SELECT * FROM $TABLE_PUNISHMENTS WHERE id=?"
    const val GET_PUNISHMENTS_BY_TARGET_NAME = "SELECT * FROM $TABLE_PUNISHMENTS WHERE target_name=?"
    const val GET_PUNISHMENTS_BY_TARGET_UUID = "SELECT * FROM $TABLE_PUNISHMENTS WHERE target_uuid=?"
    const val GET_PUNISHMENTS_BY_CREATOR_NAME = "SELECT * FROM $TABLE_PUNISHMENTS WHERE creator_name=?"
    const val WIPE_TABLE_PUNISHMENTS = "DELETE FROM $TABLE_PUNISHMENTS"

    const val CREATE_ACTION = """
        INSERT INTO $TABLE_ACTIONS (pun_id, id, type, executor, date, reason) VALUES (?,?,?,?,?,?)
    """
    const val DELETE_ACTIONS = "DELETE FROM $TABLE_ACTIONS WHERE pun_id=?"
    const val GET_PUNISHMENT_ACTION = "SELECT * FROM $TABLE_ACTIONS WHERE pun_id=? AND id=? AND type=?"
    const val GET_PUNISHMENT_ACTION_BY_TYPE = "SELECT * FROM $TABLE_ACTIONS WHERE pun_id=? AND type=?"
    const val GET_PUNISHMENT_ACTIONS = "SELECT * FROM $TABLE_ACTIONS WHERE pun_id=?"
    const val WIPE_TABLE_ACTIONS = "DELETE FROM $TABLE_ACTIONS"


    const val CREATE_PLAYER_SESSION = """
        INSERT INTO $TABLE_PLAYER_SESSIONS (uuid, name, ip, join_time, quit_time) VALUES (?,?,?,?,?)
    """
    const val WIPE_TABLE_PLAYER_SESSIONS = "DELETE FROM $TABLE_PLAYER_SESSIONS"
}