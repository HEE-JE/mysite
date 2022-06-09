package com.douzone.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.vo.GalleryVo;

@Repository
public class galleryRepository {

	@Autowired
	private SqlSession sqlSession;

	public List<GalleryVo> findAll() {
		return sqlSession.selectList("gallery.findAll");
	}

	public boolean insert(GalleryVo vo) {
		return sqlSession.insert("gallery.insert", vo) == 1;
	}

	public boolean delete(Long no) {
		return sqlSession.delete("gallery.delete", no) == 1;
	}
}