# Static site Boilerplate

This static boilerplate should give you a running start in building a static website in a dynamic way with modern technologies. It features a build system that does the following:

- Seperates content from markup (Markdown and Handlebars templates)
- Enables the use of LESS (or SCSS)
- Enables the use of CoffeeScript
- Manages depedancies and optimizes scripts (RequireJS)
- Optimizes images (OptiPNG/JPEGtran)
- Generates an appcache manifest (Confess.js)
- Facilitates testing of code (Mocha)
- Sets you up Twitter Bootstrap

## Usage

To get started with the boilerplate, create a new folder for your project and clone the boilerplate with Git

```
mkdir new_project
cd new_project
git clone https://github.com/3wks/static-site-boilerplate.git .
```

### Files and folders

- `app/` contains the source of the site, develop your site here. It's folder structure will be copied straight to the build server, unless a file or path is blacklisted in the `app/.buildignore` file (which works just like a `.gitignore` does).
- `bin/` is meant for executables to run or develop the site.
- `bin/3wks-site` the shell script that will run the webserver for the site, making sure all dependancies are installed and environment is setup.
- `content/` is the folder which contains all the content of the site, written in Markdown. For more information, see the 'Content Management' section.
- `dist/` contains a built and optimised version of the site and the folder that gets served by the webserver.
- `lib/` is the home of some general purpose code used by the build system, web server and other piece of code that finds it useful.
- `tasks/` is where the custom tasks for the build system are defined. If there is a need to extend the build system with some custom tasks, this would be the place to put them.
- `temp/` an intermediate folder used by the build system to build in, recreated every time you build. Unless you're playing around with the build system itself, there isn't much of a reason to be in there.
- `templates/` is where the HTML of the app lives, defined in Handlebars templates. These templates decide how the content of `content/` should look. For more information, see the 'Content Management' section.
- `test/` is where the app's test suite lives, powered by [Mocha](http://visionmedia.github.com/mocha/).
- `test/spec/` is the folder where your app/site specific tests should go
- `Gruntfile.js` the main configuration file for the build system and the first place to look if you want to change anything about how the site is being put together. Specifics to be found at [Grunt's documentation](https://github.com/gruntjs/grunt/wiki/Getting-started#wiki-the-gruntfile).
- `package.json` Node's standard file for managing dependencies. 
- `Procfile` a file that specifies the all the different processes required to run the site. Used by Heroku and usable with the 'foreman' gem to get the webserver going. [More information here](http://neilmiddleton.com/the-procfile-is-your-friend/)
- `server.coffee` the Node script that launches a webserver that you can use to host the website. Uses [Express](http://expressjs.com/).

### Building the site

Of course, even though the site is static, we still want to build it in a dynamic way. To do this we use [Yeoman](http://yeoman.io/) (based on [Grunt](http://gruntjs.com/)) to build the site from it's source. In order to use Yeoman, you need to have Node (0.8+) and NPM installed. To install it, please see it's site for instructions (as it has some dependancies which go beyond the scope of this document).

Once Yeoman is installed, you can build the site running the build task:

```
yeoman build
```

A temporary folder called `temp` is created in which the site is built. Once ready you can find the built version of the site in the `dist` folder.

While developing, changing markup / styling or simply tweaking content, run Yeoman's built-in server task instead. Everytime you change a file, the site will automatically be rebuilt and refresh your browser.

```
yeoman server
```

**Note:** after running the server, the `dist` folder will be gone. You will have to use the `build` task to make a proper build of the site again.


### Rebuilding of content

In a world where content is king often little changes and tweaks need to be made to the copy of the site. With changes in content only, it's unnecessary (and sometimes annoying) to completely rebuild everything. Therefore it's possible to rebuild an existing build. To rebuild the content of the site run the following task:

```
yeoman rebuild
```

**Note:** a current build (`dist` folder) will have to be present to base the rebuild off. Only html files of the build will be replaced.

## Content management

Using a custom built task (`generate-html`) for Yeoman we've seperated the site's content from the rest of the code. Content is saved in plain text files with content formatted in Markdown (just like this README). Using the content files and corresponding templates (powered by Handlebars) the html files are created that can be used to build the rest of the site.

### Content files architecture

Whatever is in one of the content files will be interpreted as Markdown and be made available under the 'content' key of the serialised object that gets passed to template.

For example, a file containing

```
3WKS is awesome sauce
```

will result in the following object being passed to the template

```
{
	"meta": {}
	"content": "3WKS is awesome sauce"
}
```

The content files that will be read and interpreted are located in the `content` folder.

#### YAML Front matter
Just like Jekyll (probably the most populair static site generator) each file can begin with a YAML block that holds meta data about the page. Whatever is in this block (beginning and ending with `---`) will be parsed as YAML and made available as an object literal under the `meta` key.

For example. A file containing

```
---
title: Rapid site launches for big business

---
3WKS is awesome sauce
```

will result in the following object being passed to the template

```
{
	"meta": {
		"title": "Rapid site launches for big business"
	},
	"content": "3WKS is awesome sauce"
}
```


#### Nested data

Of course, many pages have a lot of sections with different bits of content. In order to organise the data and make efficient templates with more complex data trees the generator supports a folder / file structure.

After having interpreted a content file, the generator looks for a folder with the same name *without file extension*. For example, after a content file `index.md` has been interpreted it will look for a folder called `index`. 

When such a folder is found, all the files found in it (with the same file extension as the original file) will also be interpreted and be made available under the filename of the child file *without file extension*.

For example, consider the following folder structure:

```
├── index.md
└── index
    └── introduction.md
```

with the `index.md`

```
---
title: Rapid site launches for big business

---
3WKS is awesome sauce
```

and `introduction.md`

```
---
title: This video explains it all

---
This video is awesome but if you don't have 2 mins to spare, here's the *short version*
```

will result in the following object being passed to the template:

```
{
	"meta": {
		"title": "Rapid site launches for big business"
	},
	"content": "3WKS is awesome sauce",
	"introduction": {
		"meta": {
			"title": "This video explains it all"
		},
		"content": "This video is awesome but if you don't have 2 mins to spare, here's the *short version*"
	}
}
```

This process is a recursive one, meaning that you can nest data practically indefinitely.


#### Lists (arrays) of data

The html generator supports a folder / file structure that results in serialisation into arrays. Quite common on sites are lists or arrays of things, for example blog posts, team members, steps, news items, etc. To be able to work well with these within templates, it's often convenient to have them be represented as arrays (mainly so they can be iterated over).

Any folder that is found within a file's corresponding folder will be interpreted as an array (as long as it doesn't belong to another file). All the files within the 'array folder' will be processed and added to an array which will be available under the folder's name. The files are processed in alphabetical order.

For example, consider the following folder structure:

```
├── index.md
└── index
	└── posts
		├── first-post.md
 	  	└── some-other-post.md
```


With `index.md` containing,

```
---
title: Rapid site launches for big business

---
3WKS is awesome sauce
```

`first-post.md` containig

```
---
title: First post ever

---
This is the first post we've ever made.
```

and `some-other-post.md` containing

```
---
title: Some other post

---
Another post, totally unrelated to the first one
```

This will result in the following object being passed to the template:

```
{
	"meta": {
		"title": "Rapid site launches for big business"
	},
	"content": "3WKS is awesome sauce",
	"posts": [
		{
			"meta": {
				"title": "First post ever"
			},
			"content": "This is the first post we've ever made"
		},
		{
			"meta": {
				"title": "Some other post"
			},
			"content": "Another post, totally unrelated to the first one"
		}
	]
}
```

### Linking up content files and templates
In order for html files to be generated, the content has to be passed to a template. The templates are powered by Handlebars and contain the html and logic to create html files that make up the site.

The template files are stored within the `templates` folder.

All the base content files must specify a `template` attribute in their YAML front matter. The attribute should represent the path to the template to be used to render the content. The path should be relative from the `templates` folder. **Note:** if the `template` attribute is not defined, the content file is ignored. 


# Setup, Running and Hosting on Heroku with Node.js
An easy way to host this site for anyone to see is by using Heroku. You can run up a single dyno (server instance) for free that is more than capable to run small sites. Once setup, deploying a new version of the app is as easy as `git push heroku` making it extremely easy to make changes or setup new servers for stuff like testing and staging.

The repo is completely setup to run on Heroku, straight out of the box. In order to run on Heroku we use a small Node.js based webserver to serve up whatever is in the `dist` folder. You can start the server by running

```
bin/3wks-site
```

in the project root folder. This command is also defined in `Procfile`, the file that is used to instruct Heroku how to run the app.

