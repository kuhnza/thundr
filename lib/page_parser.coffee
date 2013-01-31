# Module to parse template data files and return a literal object representing the data
path = require 'path'
fs = require 'fs'
yaml = require 'js-yaml'
grunt = require 'grunt'
_ = grunt.util._
handlebars = require './helped_handlebars.coffee'

yaml_front_regex = /^\s*(---[\s\S]*)---\s*/

module.exports = class PageParser
	options: 
		templates_path: ''

	constructor: (options={}) ->
		@options = _.defaults options, @options

	parse_file: (content_file) ->
		# read the file
		raw_page_data = grunt.file.read content_file
		
		# Determine what the page data is
		unless yaml_front_regex.test raw_page_data # if there is no yaml front matter
			page_meta_data = {}
			page_content = raw_page_data
		else # if there is a yaml front matter
			yaml_front = raw_page_data.match(yaml_front_regex)[1] # select the yaml of the yaml front
			page_content = raw_page_data.replace yaml_front_regex, '' # remove the entire yaml front from the page data

			try
				page_meta_data = yaml.load yaml_front
			catch error
				grunt.log.error error
				grunt.fail.warn "YAML from content file '#{content_file}' could not be parsed."
				return
		
		page_data =
			meta: page_meta_data
			content: page_content

		# see if there are any other content files are children of this one (which are located in a folder with the same name)
		possible_dir_path = content_file.substr 0, content_file.lastIndexOf '.'
		file_extension = path.extname content_file

		# if there is no other data to use, return the data we have
		return page_data unless grunt.file.isDir possible_dir_path

		# create an array of filenames that may contain data
		child_content_files = fs.readdirSync possible_dir_path

		# for every file that contains child data, add the result of parsing it to the parsed data under the filename 
		# (without extension)
		for child_content_file in child_content_files
			# if this is a directory for which there is no content file
			child_content_file = "#{possible_dir_path}/#{child_content_file}"

			if grunt.file.isDir(child_content_file) and not grunt.file.exists child_content_file + file_extension
				# ignore it, unless it's to be interpeted as an array of data
				child_dir_files = grunt.file.expandFiles "#{child_content_file}/*#{file_extension}"

				# filter out all the files that we don't want / need
				array_type_files = _.filter child_dir_files, (file) ->
					path.extname(file) is file_extension

				continue unless array_type_files.length > 0

				# there are files that have the syntax to be included into an array

				content_name = path.basename child_content_file
				page_data[content_name] = for array_type_file in array_type_files
					@parse_file array_type_file


			else
				# only parse files that have the same extension as we have handled so far
				continue unless path.extname(child_content_file) is file_extension

				content_name = path.basename child_content_file, file_extension
				page_data[content_name] = @parse_file child_content_file

		return page_data