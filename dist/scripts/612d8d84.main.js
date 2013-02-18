require.config({
	shim: {},

	paths: {
			jquery: 'vendor/jquery.min'
	}
});
 
require([], function() {
	'use strict';

	$(function() {
		// If we cross link pages to a collapsed accordian, open the entry of interest.
		// We'll also open any collapsed accordian section within the same page
		if (location.hash) { $(location.hash + '.collapse').collapse('show'); }
		$(window).bind('hashchange', function() {
			if (location.hash) { $(location.hash + '.collapse').collapse('show'); }
		});

		$('.dropdown-toggle').dropdown();
		$('.collapse').collapse();

		// with timeout, just as Bootstraps own docs use
		setTimeout(function() {
			$('.module_nav').affix({
				offset: {
					top: function() {
						return $('.module_nav_container').offset().top;
					}
				}
			});
		}, 100);
	});
	
});