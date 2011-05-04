package net.caprazzi.skimpy.serlvet;

import static org.junit.Assert.*;

import net.caprazzi.skimpy.framework.RequestInfo;

import org.junit.Test;

public class RequestInfoTest {

	@Test
	public void test_should_not_match_last_slash() {
		RequestInfo ri = new RequestInfo("GET", "/one/two/three" );
		assertTrue(ri.isPath("/one/two/three"));
		assertFalse(ri.isPath("/one/two/three/"));
		assertTrue(ri.isPath("/one/two/_"));
		assertFalse(ri.isPath("/one/two/_/"));				
	}
	
	@Test
	public void test_should_match_last_slash() {
		RequestInfo ri = new RequestInfo("GET", "/one/two/three/" );
		assertTrue(ri.isPath("/one/two/three/"));
		assertFalse(ri.isPath("/one/two/three"));
		assertTrue(ri.isPath("/one/two/_/"));
		assertFalse(ri.isPath("/one/two/_"));				
	}
	
	@Test
	public void test_should_match_exact() {
		RequestInfo ri = new RequestInfo("GET", "/one/two/three" );
		assertFalse(ri.isPath("/one/two/x"));
		assertTrue(ri.isPath("/one/two/three"));
	}
	
	@Test
	public void test_should_match_underscore_wildcard() {
		RequestInfo ri = new RequestInfo("GET", "/one/two/three" );
		assertTrue(ri.isPath("/_/_/_"));
		assertTrue(ri.isPath("/one/_/_"));
		assertTrue(ri.isPath("/one/two/_"));
		assertTrue(ri.isPath("/_/two/_"));
		assertTrue(ri.isPath("/_/_/three"));
		assertTrue(ri.isPath("/one/_/three"));
	}	
	
}
