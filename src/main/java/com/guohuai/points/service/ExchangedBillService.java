package com.guohuai.points.service;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.points.dao.ExchangedBillDao;
import com.guohuai.points.entity.ExchangedBillEntity;
import com.guohuai.points.form.ExchangedBillForm;
import com.guohuai.points.res.ExchangedBillRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExchangedBillService {
	@Autowired
	private ExchangedBillDao exchangedBillDao;

	/**
	 * 分页查询
	 *
	 * @param req
	 * @return
	 */
	public PageResp<ExchangedBillRes> page(ExchangedBillForm req) {

		Page<ExchangedBillEntity> pages = exchangedBillDao.findAll(buildSpecification(req), new PageRequest(req.getPage() - 1, req.getRows()));
		PageResp<ExchangedBillRes> resPage = new PageResp<>();

		for (ExchangedBillEntity page : pages) {
			ExchangedBillRes res = new ExchangedBillRes();
			BeanUtils.copyProperties(page, res);
			resPage.getRows().add(res);
		}
		resPage.setTotal(pages.getTotalElements());

		return resPage;
	}

	private Specification<ExchangedBillEntity> buildSpecification(ExchangedBillForm req) {
		return new Specification<ExchangedBillEntity>() {
			@Override
			public Predicate toPredicate(Root<ExchangedBillEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (!StringUtil.isEmpty(req.getUserOid())) {
					list.add(cb.like(root.get("userOid").as(String.class), req.getUserOid()));
				}

				query.where(cb.and(list.toArray(new Predicate[list.size()])));
//				query.orderBy(cb.desc(root.get("createTime")));
				return query.getRestriction();
			}
		};
	}

	public ExchangedBillRes findById(String oid) {

		ExchangedBillEntity entity = exchangedBillDao.findOne(oid);
		ExchangedBillRes billRes = new ExchangedBillRes();
		BeanUtils.copyProperties(entity, billRes);

		return billRes;
	}
}
