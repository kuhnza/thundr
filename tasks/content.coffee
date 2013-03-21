path = require 'path'
fs = require 'fs'
handlebars = require './../lib/helped_handlebars.coffee'
PageParser = require './../lib/page_parser'

module.exports = (grunt) ->
	grunt_helpers = require('grunt-lib-contrib').init(grunt);
	_ = grunt.util._

	grunt.registerMultiTask 'generate-html', 'Generate html files from the content files', ->
		options = @options
			basePath: false
			flatten: false
			page_meta_defaults:
				nav_position: null
				nav_has_overview: true


		@file.dest = path.normalize @file.dest

		page_parser = new PageParser
			templates_path: 'templates/'

		# read all the files specified in the task
		content_files = grunt.file.expand @file.src

		# create a registery for modules so that we can nest pages properly, and global pages too
		modules = []
		modules_by_name = {}
		global_pages = []

		module_name_regex = /(modules\/)([\w-_]+)\/(.+)/

		pages = for content_file in content_files
			continue unless grunt.file.isFile content_file

			module_name = content_file.match(module_name_regex)?[2]

			registery = unless module_name?
				# when no module name could be found
				global_pages
			else
				# if we matched a module name
				
				unless modules_by_name[module_name]? # unless the module is already known
					# create a new module
					module_path = content_file.replace module_name_regex, '$1$2'
					module_manifest_path = path.join module_path, 'content_module.json'

					if grunt.file.isFile module_manifest_path
						module_manifest = grunt.file.readJSON module_manifest_path
					else
						module_manifest = {}

					new_module = modules_by_name[module_name] = _.extend
						id: module_name
						name: module_name
						nav: {}
					, module_manifest,
						pages: []

					modules.push new_module

				modules_by_name[module_name].pages

			# at this point we know of which this registery should be part

			page_data = page_parser.parse_file content_file

			# we can only render pages that specify a template to do it with
			layout_path = "templates/#{page_data.meta.layout}"
			unless layout_path? and grunt.file.isFile layout_path
				grunt.log.error "Can't render page for #{content_file.cyan} because no layout has been defined"

			# determine the file path of the rendered template file
			dest_file_path = grunt_helpers.buildIndividualDest @file.dest, content_file, options.basePath, options.flatten
			absolute_dest_path = path.resolve process.cwd(), dest_file_path
			path_to_doc_root = path.relative path.dirname(absolute_dest_path), path.join process.cwd(), 'temp'
			path_to_doc_root += '/' if path_to_doc_root.length > 0
			
			# remove the destination path to be left with the relative url
			# TODO: remove hardcoded temp
			page_href = path.normalize dest_file_path.replace 'temp/', ''

			# merge the pages href into the page data (so it can be overridden by the page itself)
			page_data.meta = _.extend 
				href: page_href
				module_name: module_name
				module: modules_by_name[module_name]
				path_to_root: path_to_doc_root
			, page_data.meta

			page_data.meta._file = dest_file_path

			# override the defaults with what was parsed
			page_data.meta = _.extend {}, options.page_meta_defaults, page_data.meta

			# create the needed references to the parsed page
			registery.push page_data
			
			page_data
		
		for _module in modules # prefixing module with an underscore to not conflict with node's packages
			# sort the pages of each module
			_module.pages = _.sortBy _module.pages, (page) -> page.meta.nav_position
			modules_by_name[_module.id].pages = _module.pages

			# try and see if we can determine the link to the module
			pages_for_nav = _.filter _module.pages, (page) -> page.meta.nav_position

			# if we only have 1 page for navigation, set the href of the module to the href of the first page
			if pages_for_nav.length > 0
				_module.href = _.first(pages_for_nav).meta.href
			else
				_module.href = null

		# sort the modules and the global pages by navigation position
		modules = _.sortBy modules, (_module) -> _module.nav.position
		global_pages = _.sortBy global_pages, (page) -> page.meta.nav_position

		# for all the pages we have
		for page in pages
			page = page_parser.render_page page

			layout_data =
				modules: modules
				global_pages: global_pages
				current_page: page
				current_module: page.meta.module

			try
				template_src = grunt.file.read layout_path
				template = handlebars.compile template_src
			catch error
				grunt.log.error error
				grunt.fail.warn "Handlebars failed to compile '#{layout_path}'."

			grunt.verbose.writeln "Compiled layout #{layout_path.cyan}"

			# use the relative path
			dest_file_path = page.meta._file

			# render the template
			rendered_template = template layout_data

			grunt.file.write dest_file_path, rendered_template
			grunt.log.writeln "File '#{dest_file_path.cyan}' created."
