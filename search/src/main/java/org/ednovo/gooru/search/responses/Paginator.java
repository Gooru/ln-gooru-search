package org.ednovo.gooru.search.responses;


public class Paginator {
	
	    private int start;
	    private int end; 
    	
    	public Paginator(){}
    	
		public Paginator(int totalHits, int pageNumber, int pageSize) {
			
			if (totalHits < 1 || pageNumber < 1 || pageSize < 1) {
					this.setStart(0);
					this.setEnd(0);
			}
			else{
				int start = 1 + (pageNumber - 1) * pageSize;
				int end = Math.min(pageNumber * pageSize, totalHits);
				if (start > end) {
					start = Math.max(1, end - pageSize);
				}
				
				this.setStart(start);
				this.setEnd(end);
			}
		}

		/**
		 * 18 totalHits, pageSize 5
		 *
		 * 1: 1-5
		 * 2: 6-10
		 * 3: 11-15
		 * 4: 16-18
		 *
		 * @param totalHits
		 * @param pageNumber
		 * @param pageSize
		 * @return
		 */
		public ArrayLocation calculateArrayLocation(int totalHits, int pageNumber, int pageSize) {
				ArrayLocation al = new ArrayLocation();
				
				if (totalHits < 1 || pageNumber < 1 || pageSize < 1) {
						al.setStart(0);
						al.setEnd(0);
						return al;
				}
				
				int start = 1 + (pageNumber - 1) * pageSize;
				int end = Math.min(pageNumber * pageSize, totalHits);
				if (start > end) {
						start = Math.max(1, end - pageSize);
				}
				
				al.setStart(start);
				al.setEnd(end);
				return al;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}
}
	