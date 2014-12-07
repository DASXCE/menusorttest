package com.example.sortingtest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sun.tools.classfile.Annotation.element_value;

@Singleton
@Startup
public class MySingleton {

	@PersistenceContext
	private EntityManager em;

	private List<MenuItem> list;

	@PostConstruct
	private void init() {
		// genSampleDataLinkAllNode();

		genSampleDataLinkNodesOnSameLevel();

		list = getFromDB();

		list = sortListv2(list, new LinkedList<MenuItem>());

		displayTree(getTopLevel(list), 0);
		// System.out.println(l);

		 makeSomeChanges();
		 
		 list = getFromDB();

			list = sortListv2(list, new LinkedList<MenuItem>());

		displayTree(getTopLevel(list), 0);

	}

	public void makeSomeChanges() {
		System.out.println("making changes...");
		MenuItem drag = list.get(7);
		MenuItem drop = list.get(4);

		System.out.println("dragin " + drag.getName() + " over "
				+ drop.getName());

		// 1. change parent
		drag.setParent(drop);

		// 2. reconnect left space
		drag.getPrevious().setNext(drag.getNext());
		if (drag.getNext() != null) {
			drag.getNext().setPrevious(drag.getPrevious());
		}else {
			drag.getPrevious().setNext(null);
		}
		// update in db left space
		em.merge(drag.getPrevious());
		if (drag.getNext() != null) {
			em.merge(drag.getNext());
		}
		

		// 3. reconnect new space

		// if insert before first child
		MenuItem firstChild = getFirstChild(drop);
//		drag.setPrevious(null);
//		if (firstChild != null) {
//			drag.setNext(firstChild);
//			firstChild.setPrevious(drag);
//			em.merge(firstChild);
//		}

		// if insert between children
		 int indexOfFirstChild = list.indexOf(firstChild);
		 MenuItem newNextChild = list.get(indexOfFirstChild+1);
		 drag.setPrevious(firstChild);
		 firstChild.setPrevious(drag);
		 em.merge(firstChild);
		 drag.setNext(newNextChild);
		 
		 newNextChild.setPrevious(drag);
		 em.merge(newNextChild);
		 
		
		// update db
		em.merge(drag);
		em.merge(drop);
		System.out.println("done making changes...");
	}

	public MenuItem getFirstChild(MenuItem parent) {
		List<MenuItem> rlist = em
				.createQuery(
						"Select m from MenuItem m where m.parent = :parent and m.previous = null",
						MenuItem.class).setParameter("parent", parent)
				.getResultList();
		System.out.println(rlist);
		return rlist.get(0);
	}

	public List<MenuItem> getFromDB() {
		return em.createQuery("Select m from MenuItem m order by m.previous",
				MenuItem.class).getResultList();
	}

	public List<MenuItem> sortList(List<MenuItem> list) {
		LinkedList<MenuItem> temp = new LinkedList<MenuItem>();

		MenuItem current = list.get(0);

		while (current != null) {
			temp.add(current);
			current = current.getNext();
		}

		return temp;

	}

	public LinkedList<MenuItem> sortListv2(List<MenuItem> l,
			LinkedList<MenuItem> sorted) {

		if (l.isEmpty()) {
			return sorted;
		}

		MenuItem current = l.get(0);
		while (current != null) {
			sorted.add(current);

			// djeca
			sorted = sortListv2(getChildren(list, current), sorted);

			current = current.getNext();

		}

		return sorted;

	}

	public void displayTree(List<MenuItem> l, int level) {

		for (MenuItem menuItem : l) {

			for (int i = 0; i < level; i++) {
				System.out.print(" - ");
			}

			System.out.println(menuItem.getName());

			displayTree(getChildren(list, menuItem), ++level);
			--level;
		}

	}

	public List<MenuItem> getTopLevel(List<MenuItem> l) {
		List<MenuItem> temp = new ArrayList<MenuItem>();
		for (MenuItem menuItem : l) {
			if (menuItem.getParent() == null) {
				temp.add(menuItem);
			}
		}
		return temp;
	}

	public List<MenuItem> getChildren(List<MenuItem> l, MenuItem m) {

		List<MenuItem> temp = new ArrayList<MenuItem>();
		for (MenuItem menuItem : l) {
			if (menuItem.getParent() != null
					&& menuItem.getParent().getId() == m.getId()) {
				temp.add(menuItem);
			}
		}
		return temp;
	}

	public void genSampleDataLinkAllNode() {
		MenuItem m1 = new MenuItem("naslovna", null, null, null);
		MenuItem m2 = new MenuItem("ankete", null, null, null);
		MenuItem m3 = new MenuItem("kalendari", null, null, null);
		MenuItem m4 = new MenuItem("galerija", null, null, null);
		MenuItem m5 = new MenuItem("2014", null, null, null);
		MenuItem m6 = new MenuItem("2013", null, null, null);
		MenuItem m7 = new MenuItem("januar", null, null, null);
		MenuItem m8 = new MenuItem("februar", null, null, null);
		MenuItem m9 = new MenuItem("mart", null, null, null);
		MenuItem m10 = new MenuItem("april", null, null, null);
		MenuItem m11 = new MenuItem("kontakt", null, null, null);
		MenuItem m12 = new MenuItem("aktuelno", null, null, null);
		MenuItem m13 = new MenuItem("zabava", null, null, null);

		// 1
		m1.setNext(m2);

		// 2
		m2.setParent(m1);
		m2.setPrevious(m1);
		m2.setNext(m3);

		// 3
		m3.setParent(m1);
		m3.setPrevious(m2);
		m3.setNext(m4);

		// 4
		m4.setPrevious(m3);
		m4.setNext(m5);

		// 5
		m5.setParent(m4);
		m5.setPrevious(m4);
		m5.setNext(m6);

		// 6
		m6.setParent(m4);
		m6.setPrevious(m5);
		m6.setNext(m7);

		// 7
		m7.setParent(m5);
		m7.setPrevious(m6);
		m7.setNext(m8);

		// 8
		m8.setParent(m5);
		m8.setPrevious(m7);
		m8.setNext(m9);

		// 9
		m9.setParent(m6);
		m9.setPrevious(m8);
		m9.setNext(m10);

		// 10
		m10.setParent(m6);
		m10.setPrevious(m9);
		m10.setNext(m11);

		// 11
		m11.setPrevious(m10);
		m11.setNext(m12);

		// 12
		m12.setPrevious(m11);
		m12.setNext(m13);

		// 13
		m13.setPrevious(m12);

		em.persist(m1);
		em.persist(m2);
		em.persist(m3);
		em.persist(m4);
		em.persist(m5);
		em.persist(m6);
		em.persist(m7);
		em.persist(m8);
		em.persist(m9);
		em.persist(m10);
		em.persist(m11);
		em.persist(m12);
		em.persist(m13);

	}

	public void genSampleDataLinkNodesOnSameLevel() {
		MenuItem m1 = new MenuItem("naslovna", null, null, null);
		MenuItem m2 = new MenuItem("ankete", null, null, null);
		MenuItem m3 = new MenuItem("kalendari", null, null, null);
		MenuItem m4 = new MenuItem("galerija", null, null, null);
		MenuItem m5 = new MenuItem("2014", null, null, null);
		MenuItem m6 = new MenuItem("2013", null, null, null);
		MenuItem m7 = new MenuItem("januar", null, null, null);
		MenuItem m8 = new MenuItem("februar", null, null, null);
		MenuItem m9 = new MenuItem("mart", null, null, null);
		MenuItem m10 = new MenuItem("april", null, null, null);
		MenuItem m11 = new MenuItem("kontakt", null, null, null);
		MenuItem m12 = new MenuItem("aktuelno", null, null, null);
		MenuItem m13 = new MenuItem("zabava", null, null, null);

		// 1
		m1.setNext(m4);

		// 2
		m2.setParent(m1);
		m2.setNext(m3);

		// 3
		m3.setParent(m1);
		m3.setPrevious(m2);

		// 4
		m4.setPrevious(m1);
		m4.setNext(m11);

		// 5
		m5.setParent(m4);
		m5.setNext(m6);

		// 6
		m6.setParent(m4);
		m6.setPrevious(m5);

		// 7
		m7.setParent(m5);
		m7.setNext(m8);

		// 8
		m8.setParent(m5);
		m8.setPrevious(m7);

		// 9
		m9.setParent(m6);
		m9.setNext(m10);

		// 10
		m10.setParent(m6);
		m10.setPrevious(m9);

		// 11
		m11.setPrevious(m4);
		m11.setNext(m12);

		// 12
		m12.setPrevious(m11);
		m12.setNext(m13);

		// 13
		m13.setPrevious(m12);

		em.persist(m1);
		em.persist(m2);
		em.persist(m3);
		em.persist(m4);
		em.persist(m5);
		em.persist(m6);
		em.persist(m7);
		em.persist(m8);
		em.persist(m9);
		em.persist(m10);
		em.persist(m11);
		em.persist(m12);
		em.persist(m13);

	}
}
