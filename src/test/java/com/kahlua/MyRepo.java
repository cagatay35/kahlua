package com.kahlua;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.kahlua.DenemeEntry;
import com.kahlua.data.repository.KahluaRepository;

@Component
@Repository
public class MyRepo extends KahluaRepository<DenemeEntry>{
	
	public MyRepo() {
		super((DataSource) AppContext.getApplicationContext().getBean("dataSource"));
	}

}