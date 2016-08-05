package org.ednovo.gooru.search.domain.service;


public class DigestContext {
		/*private String classPlanId;*/
		/*private String topic;*/
		/*private String lesson;*/
		private String vocabulary;
        private StringBuffer classPlanContent= new StringBuffer("");
		private StringBuffer segmentTitles= new StringBuffer("");
		
		private IndexData indexData = new IndexData();
		
		/*public String getClassPlanId(){
			return this.classPlanId;
		}
		
		public void setClassPlanId(String content) {
			this.classPlanId = content;
			addIndexDataTerm("id",this.classPlanId,true,true, 0.25f);
		}
		*/
		/*public void setTopic(String content) {
			this.topic = content;
			addIndexDataTerm("topic",this.topic,true,true, 2f);
		}
		
		public void setLesson(String content) {
			this.lesson = content;
			addIndexDataTerm("lesson",this.lesson,true,true, 3.5f);
		}*/
		
		public void setVocabulary(String content) {
			this.vocabulary = content;
			addIndexDataTerm("vocabulary",this.vocabulary,true,true, 2f);
		}
				
		public void appendContent(String pLine) {
			this.setClassPlanContent(pLine);
		}
		
		public void appendSegmentTitles(String pLine) {
			this.setSegmentTitles(pLine);
		}

		public String getClassplanContent() {
			 return this.classPlanContent.toString();
		}
		
		public void setClassPlanContent(String content) {
			this.classPlanContent.append(" ");
			this.classPlanContent.append(content);
		}

		public String getSegmentTitles() {
			return this.segmentTitles.toString();
		}
	   
		public void setSegmentTitles(String content) {
		   this.segmentTitles.append(" ");
		   this.segmentTitles.append(content);
		}
		
		public void addIndexDataTerm(String fieldName, String fieldContent, boolean fieldStore, boolean fieldAnalyzed, float fieldBoost){
			IndexDataTerm indexDataTerm = new IndexDataTerm();
			indexDataTerm.setFieldName(fieldName);
			indexDataTerm.setFieldContent(fieldContent);
			indexDataTerm.setFieldStore(fieldStore);
			indexDataTerm.setFieldAnalyzed(fieldAnalyzed);
			indexDataTerm.setFieldBoost(fieldBoost);
		
			this.indexData.addIndexDataTerm(indexDataTerm);
		}
		
		public IndexData getIndexData(){
			
			addIndexDataTerm("segmentTitles",this.segmentTitles.toString(),false,true, 1.5f);
			addIndexDataTerm("classplanContent",this.classPlanContent.toString(),false,true, 1f);
			
			return this.indexData;
		}
     
	}