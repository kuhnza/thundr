path = require 'path'
fs = require 'fs'

module.exports = (grunt) ->
	grunt_helpers = require('grunt-lib-contrib').init(grunt);

	grunt.task.registerTask 'rebuild:content', 'Takes the current build and regenerates the html', ->
		@requiresConfig 'output', 'staging'
		
		options = @options()


		current_build_path = grunt.config 'output'
		if not grunt.file.isDir current_build_path
			grunt.fail.warn "There is no current build to rebuild the html for. Perform a normal build instead"
			return


		# We have a current build, now let's rebuild it.
		tasks = ['clean:rebuild', 'copy:rebuild:pre', 'generate-html', 'generate-html:post', 'usemin-handler', 'usemin', 'copy:rebuild:post', 'time']

		grunt.task.run tasks

	grunt.task.registerTask 'generate-html:post', ->
		# save the current working directory, as other tasks need to update the base, and we might want to go back to it later
		base = grunt.config('base') or grunt.option('base') or process.cwd()
		grunt.config 'base', base

		# set the base path to the staging folder so all other tasks work properly
		grunt.file.setBase grunt.config 'staging'

	grunt.task.registerTask 'copy:rebuild:pre', 'Copies the whole build folder to the staging folder', ->
		@requiresConfig 'output', 'staging'
		config = grunt.config()
		done = @async()

		ignores = ['.gitignore', '.ignore', '.buildignore']

		grunt.task.helper 'copy', config.output, config.staging, ignores, (error) ->
			if error
				grunt.log.error error.stack or error.message
			else
				grunt.log.ok "#{path.resolve(config.output)} -> #{path.resolve(config.staging)}"

			done !error

	grunt.task.registerTask 'copy:rebuild:post', 'Copies the new html files back to the build folder', ->
		@requiresConfig 'output', 'staging'
		config = grunt.config()

		# set the base back to cwd
		grunt.file.setBase config.base

		files = grunt.file.expandFiles "#{config.staging}/*.html"

		for file in files
			dest = grunt_helpers.buildIndividualDest "#{config.output}/*.html", file, config.staging, false
			
			grunt.file.copy file, dest
			grunt.log.ok "#{path.resolve file} -> #{path.resolve dest}"

	grunt.task.registerTask 'clean:rebuild', 'Wipe the staging build dir', ->
		@requiresConfig 'staging'

		grunt.task._helpers.rimraf grunt.config 'staging'
