package ca.usask.cs.srlab.simcad.index;

import java.io.Serializable;
import java.util.Arrays;

public class IndexKey<T extends Number> implements Serializable {

		private static final long serialVersionUID = 9206469927523956457L;
		
		/** The individual keys */
	    private final T[] keys;
	    /** The cached hashCode */
	    private final int hashCode;
	    
	    /**
	     * Constructor taking two keys.
	     * 
	     * @param key1  the first key
	     * @param key2  the second key
	     */
	    @SuppressWarnings("unchecked")
		public IndexKey(T key1, T key2) {
	        this(false, key1, key2);
	    }
	    
	    
	    /**
	     * Constructor taking an array of keys.
	     * <p>
	     * If the array is not copied, then it must not be modified.
	     *
	     * @param keys  the array of keys
	     * @param makeCopy  true to copy the array, false to assign it
	     * @throws IllegalArgumentException if the key array is null
	     */
	    protected IndexKey(boolean makeCopy, T...keys) {
	        super();
	        if (keys == null) {
	            throw new IllegalArgumentException("The array of keys must not be null");
	        }
	        if (makeCopy) {
	            this.keys = (T[]) keys.clone();
	        } else {
	            this.keys = keys;
	        }
	        
	        int total = 0;
	        for (int i = 0; i < keys.length; i++) {
	            if (keys[i] != null) {
	                if (i == 0) {
	                    total = keys[i].hashCode();
	                } else {
	                    total ^= keys[i].hashCode();
	                }
	            }
	        }
	        hashCode = total;
	    }
	    
	    /**
	     * Gets a copy of the individual keys.
	     * 
	     * @return the individual keys
	     */
	    public T[] getKeys() {
	        return (T[]) keys.clone();
	    }
	    
	    public T getLineKey(){
	    	return keys[0];
	    }
	    
	    public T getBitKey(){
	    	return keys[1];
	    }
	    
	    /**
	     * Compares this object to another.
	     * <p>
	     * To be equal, the other object must be a <code>MultiKey</code> with the
	     * same number of keys which are also equal.
	     * 
	     * @param other  the other object to compare to
	     * @return true if equal
	     */
	    @SuppressWarnings("unchecked")
		public boolean equals(Object other) {
	        if (other == this) {
	            return true;
	        }
	        if (other instanceof IndexKey) {
	        	IndexKey<T> otherMulti = (IndexKey<T>) other;
	            return Arrays.equals(keys, otherMulti.keys);
	        }
	        return false;
	    }

	    /**
	     * Gets the combined hash code that is computed from all the keys.
	     * <p>
	     * This value is computed once and then cached, so elements should not
	     * change their hash codes once created (note that this is the same 
	     * constraint that would be used if the individual keys elements were
	     * themselves {@link java.util.Map Map} keys.
	     * 
	     * @return the hash code
	     */
	    public int hashCode() {
	        return hashCode;
	    }

	    /**
	     * Gets a debugging string version of the key.
	     * 
	     * @return a debugging string
	     */
	    public String toString() {
	        return "MultiKey" + Arrays.asList(keys).toString();
	    }

	}
