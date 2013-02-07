# Thundr Docs
Documentation site for the thundr web framework by 3wks. Generated dynamically from documentation written in Markdown. Based on 3wks' [static site boilerplate][1].


## Usage

### Most important files and folders

Below you'll find a description of the most important files and folders. For full documentation, have a look at the [static site boilerplate][1].

- `app/` contains the source of the site, develop your site here. It's folder structure will be copied straight to the build server, unless a file or path is blacklisted in the `app/.buildignore` file (which works just like a `.gitignore` does).
- `content/` is the folder which contains all the content of the site, written in Markdown. For more information, see the 'Content Management' section.
- `dist/` contains a built and optimised version of the site and the folder that gets served by the webserver.
- `temp/` an intermediate folder used by the build system to build in, recreated every time you build. Unless you're playing around with the build system itself, there isn't much of a reason to be in there.
- `templates/` is where the HTML of the app lives, defined in Handlebars templates. These templates decide how the content of `content/` should look. For more information, see the 'Content Management' section.

### Building the site

We use [Yeoman](http://yeoman.io/) (based on [Grunt](http://gruntjs.com/)) to build the site from it's source. In order to use Yeoman, you need to have Node (0.8+) and NPM installed. To install it, please see it's site for instructions (as it has some dependancies which go beyond the scope of this document).

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

For full documentation on the building process refer to the [static site boilerplate documentation][1].


### Publishing the documentation

We do not yet have publishing of the docs figured out. But what seems most interesting is using subtree merging (using [subslit](https://github.com/dflydev/git-subsplit/blob/master/git-subsplit.sh)?) to push the `dist` folder to [the 'gh-pages' branch of the thundr repo](https://github.com/3wks/thundr/tree/gh-pages). This way we could:

- host the site on Github Pages, where it is at the moment
- publish changes with a single command
- still dynamically build the docs

## Content management

Using a custom built task (`generate-html`) for Yeoman we've seperated the site's content from the rest of the code. Content is saved in plain text files with content formatted in Markdown (just like this README). Using the content files and corresponding templates (powered by Handlebars) the html files are created that can be used to build the rest of the site.

### Content files architecture

For documentation of the actual content files, see the [static site boilerplate's documentation][1].

### Pages

Every Markdown file that's a direct child of the `content` folder will be interpreted as a seperate page and rendered to html. E.g. the file `content/basics.md` will be generated and saved as `basics.html` in the root of the build folder. There is currently no support for placing the files anywhere else.

All content files define the `layout` attribute in their YAML front matter to determine what layout should be used to render them. By default this is `layout.hbs` which refers to the template in the `templates` folder. This template holds the overall layout and navigation. When `layout` isn't defined, the content file will not be rendered.

For a content file to be rendered as a Page (with support for Sections (see next section)) it should have the `template` attribute of it's YAML front matter defined as `page.hbs`. The `id` attribute should also be defined so that the rest of the site knows how to reference the page. Furthermore, defining `title` will asure for a human friendly name of the id and is used by the page template, navigation, etc.

**Meta information reference**

| Attribute 		| Description
| ----------------- | -------------------------------
| `layout`			| **required** path to the layout template (relative to template path). Use `layout.hbs` for the normal layout 
| `template`		| **required** path to the page template (relative to template path). Use `page.hbs` for the normal page
| `id` 				| **required** unique identifier for this page
| `title` 			| Human friendly name of the page. Used for the site's title, in navigation, etc.
| `description`		| Description of the page, used in the page's meta tags
| `nav_position`	| *integer* when set the position in the navigation bar. When not set (or falsey) the page is excluded from the navigation
| `nav_title`		| Human friendly title that will be used in the navigation. When not set, the Page's full `title` is used. Useful when the Page's title is lengthy
| `nav_has_overview` | *boolean* when falsy a sub-navigation will not have an 'overview' item rendered. By default, it will


#### Sections

Each Page can have Sections. Sections are parts of a Page that are referable by id. This allows deep linking to certain bits of documentation. They are defined in their own content files. When a Page has Sections a dropdown menu will be generated in the navigation (see next section) containing deep links to these sections.

To add a Section to a Page, add a new content file at the following path `<content_filename_without_ext>/sections/`. For example, to add a Section to the basics page create the file `basics/sections/new_section.md`.

Sections are automatically included in the Page and sorted alphabetically. A good tip to easily manage the order they are in is by prefixing the filenames with a number like `001`, `002`, `003`, etc.

**Meta information reference**

| Attribute 		| Description
| ----------------- | -------------------------------
| `id`				| **required** unique identifier for this section. Used in the navigation and section tag to reference it
| `title`			| Human friendly title of section, used in the navigation
| `starts_new_category` | *boolean* when truthy it marks a new category in sections. Basically just lets you add a divider line above the section in the navigation
| `is_advanced_topic` | *boolean* when truthy it marks a section as an advanced topic. Basically just adds an 'advanced' label next to the section's title in the navigation


#### Navigation 

The navigation is automatically generated in the `layout.hbs` template. A Page has to opt-in to be included into the navigation by defining `nav_position`, a number determining it's position in the navigation bar, in it's YAML front matter. The Page's `title` will be used as the text representing the navigation item.

When a Page has Sections, a dropdown menu will be generated containing an item and deeplink for each Section. It uses the Section's `title` attribute as the text for the item.



# Setup, Running and Hosting on Heroku with Node.js
An easy way to host this site for anyone to see is by using Heroku. You can run up a single dyno (server instance) for free that is more than capable to run small sites. Once setup, deploying a new version of the app is as easy as `git push heroku` making it extremely easy to make changes or setup new servers for stuff like testing and staging.

The repo is completely setup to run on Heroku, straight out of the box. In order to run on Heroku we use a small Node.js based webserver to serve up whatever is in the `dist` folder. You can start the server by running

```
bin/3wks-site
```

in the project root folder. This command is also defined in `Procfile`, the file that is used to instruct Heroku how to run the app.


[1]: https://github.com/3wks/static-site-boilerplate

