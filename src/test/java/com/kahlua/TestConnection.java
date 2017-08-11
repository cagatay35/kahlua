package com.kahlua;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={TestConfig.class})
@ContextConfiguration(locations = "/applicationContext.xml")
@Component
@ComponentScan(basePackages = {"com.kahlua"})
public class TestConnection extends TestCase {
	@Autowired
	MyRepo repo;
	
	static DenemeEntry t = new DenemeEntry();
	static long count = 0;
	
	@Before
    public void setUp() throws Exception {
    	t.setEntry("ezgi");  	
    }
	
	
	@After
    public void after() throws SQLException{
    	repo.deleteAll();
    	repo.resetSequenceValue("SEQ_DENEME");
    	
    }
	

	@Test
    public void testDeleteAll() throws SQLException{
		repo.deleteAll();
    	assertTrue(repo.count()==0);
    }
    
	@Test
    public void testCount() throws SQLException{
		repo.insert(t);
    	assertTrue(repo.count()==repo.findAll().size());
    }
	
	@Test
	public void testSequenceIsMore() throws SQLException{
    	assertTrue(repo.getSequenceNextValue("SEQ_DENEME")==repo.findAll().size()+1);
	}
	
	@Test
	public void testInsert() throws SQLException{
		repo.insert(t);
    	assertTrue(repo.exists(1));
	}
	
	
	@Test
	public void testFindById() throws SQLException{
		repo.insert(t);

		DenemeEntry e = repo.findById(1);
    	assertTrue(e.getEntry().equals("ezgi"));
	}
	
	@Test
	public void testUpdate() throws SQLException{
		repo.insert(t);
		t.setEntry("tek");
		repo.updateById(t);
    	assertTrue(repo.findById(1).getEntry().equals("tek"));
	}

	@Test
    public void testFindAll() throws SQLException{
		repo.insert(t);
		List<DenemeEntry> res = repo.findAll();
		assertTrue(res.get(0).getEntry().equals("ezgi"));		
    }
	
	@Test
    public void testDeleteById() throws SQLException{
		repo.insert(t);
		repo.deleteById(1);
		
		//assertFalse(repo.exists(1));
		
    }
	
	@Test
    public void testExists() throws SQLException{
		repo.insert(t);		
		assertTrue(repo.exists(1));
		
    }
    
    
 

}
