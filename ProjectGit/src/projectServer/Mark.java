package projectServer;

public enum Mark {
    EMP, XXX, OOO;

    /*@
       ensures this == Mark.XXX ==> \result == Mark.OOO;
       ensures this == Mark.OOO ==> \result == Mark.XXX;
       ensures this == Mark.EMP ==> \result == Mark.EMP;
     */
    /**
     * Returns the other mark.
     * 
     * @return the other mark is this mark is not EMPTY or EMPTY
     */
    public Mark other() {
        if (this == XXX) {
            return OOO;
        } else if (this == OOO) {
            return XXX;
        } else {
            return EMP;
        }
    }
	
}
