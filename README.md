# thundr docs

Thundr's docs are deployed as a GitHub page and use Liquid templates running atop Jekyll. This means
that if you want to preview changes in your browser before checking them in they you'll need to install 
Jekyll.

In a perfect world this should be as simple as:

```bash
gem install jekyll
cd <docs dir>
jekyll serve --watch
```

If that works for you, great! Head off for an early lunch. Sadly I found that the default Ruby environment 
setup on Mavericks is somewhat lacking. If you're like me and don't have an up to date ruby install then 
do this first:

```bash
sudo gem update --system
```

Also you'll need a C++ compiler so Gem can compile native extensions. Install 
[Homebrew](https://github.com/mxcl/homebrew/wiki/Installation) if you haven't already then run:

```bash
brew update
brew install automake
# install the XCode command line tools 
xcode-select --install
# agree to the XCode license
sudo xcodebuild -license 
```

Now you can *finally* run "gem install jekyll" and it should just work.

To start the preview server cd into the project dir and run:

```bash
jekyll serve --watch
```

Simple right? ;-)
