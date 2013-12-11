'use strict';

$(document).ready(function() {
	// If we cross link pages to a collapsed accordian, open the entry of interest.
	// We'll also open any collapsed accordian section within the same page
	if (location.hash) { $(location.hash + '.collapse').collapse('show'); }
	$(window).bind('hashchange', function() {
		if (location.hash) { $(location.hash + '.collapse').collapse('show'); }
	});

	$('.dropdown-toggle').dropdown();

	// with timeout, just like Bootstraps own docs
	setTimeout(function() {
		var $nav = $('.module_nav');
			$nav
				.affix()
				.width($nav.width());
	}, 100);

	$('.collapse').on('shown hidden', function() {
		$('.module_nav').scroll('refresh');
	});	
});