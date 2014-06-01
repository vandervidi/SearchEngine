
public class IndexFileElement implements Comparable<IndexFileElement>{
private String m_word;
private int m_docNum;
private int m_frequency;

	public IndexFileElement(String w, int dn){
		this.m_word = w;
		this.m_docNum = dn;
		this.m_frequency = 1; //When creating an object of this class, Frequency is not know.
	}
	public IndexFileElement(String w, int dn,int fr){
		this.m_word = w;
		this.m_docNum = dn;
		this.m_frequency = fr; //When creating an object of this class, Frequency is not know.
	}
	
	//Getters and setters
	public String getM_word() {
	return m_word;
	}
	
	public void setM_word(String m_word) {
		this.m_word = m_word;
	}
	
	public int getM_docNum() {
		return m_docNum;
	}
	
	public void setM_docNum(int m_docNum) {
		this.m_docNum = m_docNum;
	}
	
	public int getM_frequency() {
		return m_frequency;
	}
	
	public void setM_frequency(int m_frequency) {
		this.m_frequency = m_frequency;
	}

	@Override
	public String toString() {
		return "IndexFileElement [m_word=" + m_word + ", m_docNum=" + m_docNum
				+ ", m_frequency=" + m_frequency + "]\n";
	}

	@Override
	public int compareTo(IndexFileElement next) {
		return m_word.compareTo(next.m_word);
		
	}
	
}
