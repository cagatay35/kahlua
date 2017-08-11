package com.kahlua;

import com.kahlua.data.orm.DbColumn;
import com.kahlua.data.orm.DbTable;

@DbTable(name="deneme",schema="cgokcel")
public class DenemeEntry  {	
	private long id;
	private String entry;
	
	@DbColumn(idColumn=true,name="ID",sequence="SEQ_DENEME")
	
	//TODO: ID'yi bir yerlerde sequence'a set etmek (update, delete by id vs için)
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	
	@DbColumn(idColumn=false,name="ENTRY")
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
}