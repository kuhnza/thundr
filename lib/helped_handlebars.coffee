marked = require 'marked'
handlebars = require 'handlebars'
hljs = require 'highlight.js'

marked.setOptions
	gfm: true
	breaks: true
	langPrefix: 'language-'
	highlight: (code, lang) -> hljs.highlight(lang, code).value

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

module.exports = handlebars