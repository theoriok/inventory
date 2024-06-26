#!/usr/bin/env bash
docker-compose down --remove-orphans
docker run --rm --privileged alpine hwclock -s
cleanup_images
docker-compose up

function cleanup_images() {
  if [ ! -z "$(docker images -f 'dangling=true' -q)" ]; then
     docker image prune -f
  fi
}