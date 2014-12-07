package sortingtest;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.sortingtest.MenuItem;
import com.example.sortingtest.MySingleton;

@RunWith(Arquillian.class)
public class MyTest {

	@PersistenceContext
	private EntityManager em;
	
	@Deployment
	public static JavaArchive deployment() {

		return ShrinkWrap
				.create(JavaArchive.class)
				.addClass(MenuItem.class)
				.addClass(MySingleton.class)
				.addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private MySingleton mySingleton;
	
	@Test
	public void testGetTopLeveElements() {
		List<MenuItem> l = em.createQuery("Select m from MenuItem m", MenuItem.class).getResultList();
		MenuItem naslovna = em.find(MenuItem.class, 1l);
		
		assertEquals(5, mySingleton.getTopLevel(l).size());
	}
	
	@Test
	public void testGetChildren() {
		List<MenuItem> l = em.createQuery("Select m from MenuItem m", MenuItem.class).getResultList();
		MenuItem naslovna = em.find(MenuItem.class, 1l);
		MenuItem m9 = em.find(MenuItem.class, 9l);
		
		assertNotNull(naslovna);
		assertEquals(naslovna.getName(), "naslovna");
		
		assertEquals(2, mySingleton.getChildren(l, naslovna).size());
		assertEquals(0, mySingleton.getChildren(l, m9).size());
	}
	
	@Test
	public void testListFromDB() {
		List<MenuItem> l = mySingleton.getFromDB();
		assertEquals("naslovna", l.get(0).getName());
	}
	
	@Test
	@Ignore
	public void testSorting() {
		List<MenuItem> ldb = mySingleton.getFromDB();
		List<MenuItem> l = mySingleton.sortList(ldb);
		
		assertEquals("naslovna",l.get(0).getName());
		assertEquals("kalendari",l.get(2).getName());
		assertEquals("galerija", l.get(3).getName());
		assertEquals("april",l.get(9).getName());
		assertEquals("zabava",l.get(12).getName());
		
	}
	
	@Test
	public void testGetFirstChild(){
		List<MenuItem> l = mySingleton.getFromDB();
		
		MenuItem parent = l.get(0);
		MenuItem firstChild = mySingleton.getFirstChild(l.get(0));
		
		assertNotNull(firstChild);
		assertEquals("ankete" ,firstChild.getName());
	}
}
