express = require 'express'

# options
port = process.env.PORT or 5000


# Configuring the app
app = express()

app.use express.logger 'tiny'
app.use express.compress()
app.use express.favicon "#{__dirname}/dist/favicon.ico"
app.use	app.router
app.use express.static 'dist'

# error handler for anything but the development environment
unless app.get('env') is 'development'
	app.use (err, req, res, next) ->
		if err then res.send 500, 'Internal Server Error'
		else next()

# 404 handler
app.use (req, res) -> res.send 404, 'Not Found'


# Routes
# ------
app.get '/thundr', (req, res) -> res.redirect 'http://3wks.github.com/thundr/'


# And gooo!
# ---------
app.listen port, -> console.log "Webserver listening on port #{port}"
