/**
 * <pre>
 * 给jquery加上观察者模式，仅1.7+支持
 * </pre>
 * 
 * <b>© 2011-2012 版权所有 深圳市海云天科技股份有限公司</b>
 * .Dual licensed under the MIT and GPL
 * licenses. http://docs.jquery.com/License
 * 
 * @author 李贵庆
 * @date 2014-9-19
 * @version 1.0.0
 */


(function() {
	"use strict";
	define([ 'jquery' ], function($) {
		return function() {
			var o = $({});
			this.subscribe = function() {
				o.on.apply(o, arguments);
			};

			this.unsubscribe = function() {
				o.off.apply(o, arguments);
			};

			this.publish = function() {
				o.trigger.apply(o, arguments);
			};
		};
	});

})();