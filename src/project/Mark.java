package project;

public enum Mark {
    EMP, XXX, OOO;

    /*@
       ensures this == Mark.XX ==> \result == Mark.OO;
       ensures this == Mark.OO ==> \result == Mark.XX;
       ensures this == Mark.EE ==> \result == Mark.EE;
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
