*.txt = svn:mime-type=text/plain
*.html = svn:mime-type=text/html
*.css = svn:mime-type=text/css
*.png = svn:mime-type=image/png
*.jpg = svn:mime-type=image/jpeg

for file in `find doc|grep .html$`; do svn propset svn:mime-type text/html $file; done
for file in `find doc|grep .css$`; do svn propset svn:mime-type text/css $file; done
for file in `find doc|grep .jpg$`; do svn propset svn:mime-type image/jpeg $file; done
for file in `find doc|grep .png$`; do svn propset svn:mime-type image/png $file; done

