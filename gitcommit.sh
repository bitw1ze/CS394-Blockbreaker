#!/bin/bash
comment=$*
git add src res gen assets tools Android* default.properties proguard.cfg git* README
git commit -m "$comment"
