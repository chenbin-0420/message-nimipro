package com.dhcc.nimiprogram.controller;

import com.dhcc.basic.controller.BaseController;
import com.dhcc.nimiprogram.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
controller层主要为页面服务，一般跟页面是一对一关系，跟service是多对多关系，
此生成的controller只是做一个例子，并不代表controller跟service是一对一。
为了跟菜单权限整合，一般要求每个菜单的入口页面跟controller是一对一关系！此入口页面内的子页面也还是在此controller内。

controller层主要负责request参数的反序列化和response输出的序列化。
绝对不要把序列化和反序列化的动作放到service层里！
这会让controller/service分层失去意义，让service失去复用性，降低业务间调用的连贯性！
也不要随便把页面的dto直接丢给service，让service跟页面出现强关联，这样service的复用性和稳定性也会大大降低。

一般事务在service层上，如果一个contrller方法里几个service的动作需要在一个事务里，
则应该把这几个动作先放在某一个service的某一个方法里。
如果几个service的动作不需要在一个事务里，则可分多句写在contrller里。

*/

/**
 * test_user-Controller
 * @author cb
 * @since 2020-06-23
 */
@RequestMapping(value = "com/dhcc")
@RestController
public class TestUserController extends BaseController {
	@Autowired
	private TestUserService service;

}
