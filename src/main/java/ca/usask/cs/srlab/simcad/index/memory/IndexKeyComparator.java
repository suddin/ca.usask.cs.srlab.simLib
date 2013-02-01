package ca.usask.cs.srlab.simcad.index.memory;

import java.util.Comparator;

public class IndexKeyComparator implements Comparator<IndexKey> {
		@Override
		public int compare(IndexKey key1, IndexKey key2) {
			if(key1.getLineKey().intValue() < key2.getLineKey().intValue())
				return -1;
			else if(key1.getLineKey().intValue() == key2.getLineKey().intValue()){
				if(key1.getBitKey().intValue() < key2.getBitKey().intValue())
					return -1;
				else if(key1.getBitKey().intValue() == key2.getBitKey().intValue())
					return 0;
				else
					return 1;
			}else{
				return 1;
			}
		}
}
