package pw.honu.dvs;

public enum MatchState {

    /**
     * Match is still in the lobby
     */
    PRE_GAME,

    /**
     * Players are currently gathering their materials
     */
    GATHERING,

    /**
     * Monsters are spawning
     */
    RUNNING,

    /**
     * All players died
     */
    POST_GAME

}
