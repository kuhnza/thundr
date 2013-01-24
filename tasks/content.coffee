path = require 'path'
fs = require 'fs'
marked = require 'marked'
handlebars = require 'handlebars'
yaml = require 'js-yaml'

module.exports = (grunt) ->
	grunt_helpers = require('grunt-lib-contrib').init(grunt);
	_ = grunt.util._
	parse_page_data = require './../lib/template_data.coffee'


	grunt.registerMultiTask 'generate-html', 'Generate html files from the content files', ->
		options = @options
			basePath: false
			flatten: false


		@file.dest = path.normalize @file.dest

		# read all the files specified in the task
		content_files = grunt.file.expand @file.src

		# for each task
		for content_file in content_files
			# nothing we can do if this is not a file
			continue unless grunt.file.isFile content_file

			page_data = parse_page_data content_file

			grunt.verbose.writeln "Prepared data tree for #{content_file.cyan}"

			# we can only render pages that specify a template to do it with
			template_path = "templates/#{page_data.meta.template}"
			unless template_path? and grunt.file.isFile template_path
				grunt.log.error "Can't render page for #{content_file.cyan} because no template has been defined"

			try
				template_src = grunt.file.read template_path
				template = handlebars.compile template_src
			catch error
				grunt.log.error error
				grunt.fail.warn "Handlebars failed to compile '#{template_path}'."

			grunt.verbose.writeln "Compiled template #{template_path.cyan}"

			# determine the file path of the rendered template file
			dest_file_path = grunt_helpers.buildIndividualDest @file.dest, content_file, options.basePath, options.flatten

			# render the template
			rendered_template = template page_data

			grunt.file.write dest_file_path, rendered_template
			grunt.log.writeln "File '#{dest_file_path.cyan}' created."

	# setup a helper for Handlebars so that Markdown can be parsed within templates
	handlebars.registerHelper 'marked', (content) ->
		return marked content

	# wrap content in an header tag and parse the markdown
	handlebars.registerHelper 'marked-heading', (size, content) ->
		heading = "<h#{size}>#{content}</h#{size}>"
		return marked heading

	# make the iteration index available for each blocks
	handlebars.registerHelper 'each', (content, options) ->
		result = ""

		result = (for item, i in content
			item._i = i+1
			options.fn item
		).join('')

	handlebars.registerHelper 'equal', (value1, value2, options) ->
		options.fn(this) if value1 is value2

	# conditional that checks whether this is the first element that's being iterated over
	handlebars.registerHelper 'first', (options) ->
		return unless this._i?
		options.fn(this) if this._i is 1